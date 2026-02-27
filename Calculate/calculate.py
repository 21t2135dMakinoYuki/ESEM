import sqlite3
import pandas as pd
import numpy as np
import re
import glob
import os

mouseevent_query = '''
    SELECT Document.title, Event.type, Event.eventID, Event.isTrusted, MouseEvent.mouseeventID, MouseEvent.client_X, MouseEvent.client_Y, MouseEvent.pageY, Event.epochMillis
    FROM Log
    INNER JOIN Event ON Log.logID = Event.ref
    INNER JOIN UIEvent ON Event.eventID = UIEvent.ref
    INNER JOIN MouseEvent ON UIEvent.uieventID = MouseEvent.ref
    INNER JOIN Node ON Event.eventID = Node.ref
    LEFT OUTER JOIN Document ON Node.nodeID = Document.ref
    WHERE Event.type = 'click' or Event.type = 'mousemove'
    ORDER BY Event.epochMillis ASC
'''

page_transition_query = '''
    SELECT Document.referrer, Document.URL, Document.hidden, Document.documentID, Event.eventID, Event.epochMillis
    FROM Log
    INNER JOIN Event ON Log.logID = Event.ref
    INNER JOIN Node ON Event.eventID = Node.ref
    LEFT OUTER JOIN Document ON Node.nodeID = Document.ref
    WHERE Event.type = 'visibilitychange'
    ORDER BY Event.epochMillis ASC
'''

inputevent_query = '''
    SELECT Document.title, Event.type, Event.eventID, InputEvent.inputeventID, InputEvent.data, Event.epochMillis
    FROM Log
    INNER JOIN Event ON Log.logID = Event.ref
    INNER JOIN UIEvent ON Event.eventID = UIEvent.ref
    LEFT OUTER JOIN InputEvent ON UIEvent.uieventID = InputEvent.ref
    INNER JOIN Node ON Event.eventID = Node.ref
    LEFT OUTER JOIN Document ON Node.nodeID = Document.ref
    WHERE Event.type = 'input' or Event.type = 'mousemove'
    ORDER BY Event.epochMillis ASC
    
'''

test_result_query = '''
    SELECT Document.title, Event.eventID, Event.epochMillis
    FROM Log
    INNER JOIN Event ON Log.logID = Event.ref
    INNER JOIN Node ON Event.eventID = Node.ref
    LEFT OUTER JOIN Document ON Node.nodeID = Document.ref
    WHERE Event.type = 'mousemove'
    ORDER BY Event.epochMillis ASC
    
'''

def get_mouseevent_from_db(db_path):
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    cursor.execute(mouseevent_query)
    df = pd.DataFrame(cursor.fetchall(),
                      columns=['title', 'type', 'eventID', 'isTrusted', 'mouseeventID', 'client_X', 'client_Y', 'pageY','epochMillis'])
    conn.close()
    return df

def get_inputevent_from_db(db_path):
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    cursor.execute(inputevent_query)
    df = pd.DataFrame(cursor.fetchall(), columns=['title', 'type', 'eventID', 'inputeventID', 'data', 'epochMillis'])
    conn.close()
    return df

def get_page_transition_from_db(db_path):
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    cursor.execute(page_transition_query)
    df = pd.DataFrame(cursor.fetchall(), columns=['referrer', 'URL', 'hidden', 'documentID', 'eventID', 'epochMillis'])
    conn.close()
    return df

def get_test_result_from_db(db_path):
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    cursor.execute(test_result_query)
    df = pd.DataFrame(cursor.fetchall(), columns=['title', 'eventID', 'epochMillis'])
    conn.close()
    df = df[df['title'].str.contains('Finish', na=False)]
    return df

def add_test_names(df):
    df.insert(0, 'test_name', '')
    pre_test_name = ''
    for index, row in df.iterrows():
        if (index == 0):
            pre_test_name = row['title'].split(":", 1)[1].strip()
        title = row['title']
        if (title is None):
            df.at[index, 'test_name'] = pre_test_name
        else:
            test_name = title.split(":", 1)[1].strip()
            df.at[index, 'test_name'] = test_name
            pre_test_name = test_name
    return df

def calc_mouse_move_distances(df):
    df['delta_X'] = df['client_X'].diff()
    df['delta_Y'] = df['client_Y'].diff()
    df.loc[df['type'] == 'mousemove', ['delta_X', 'delta_Y']] = 0
    df['move_distance'] = np.sqrt(df['delta_X']**2 + df['delta_Y']**2)
    result = df.groupby('test_name')['move_distance'].sum()
    result.name = 'total_move_distance'
    return result

def calc_mouse_scroll_distances(df_click, df_page_transition):
    df_click['scroll_top_distance'] = df_click['pageY'] - df_click['client_Y']
    df_click['scroll_distance'] = df_click['scroll_top_distance'].diff()
    df_click.loc[df_click['type'] == 'mousemove', ['scroll_top_distance', 'scroll_distance']] = 0
    for i in range(1, len(df_click)):
        prev = df_click.iloc[i - 1] if i > 0 else None
        curr = df_click.iloc[i]
        if (prev['type'] != 'click' or curr['type'] != 'click' or curr['scroll_distance'] == 0):    
            continue
        for epoch in df_page_transition["epochMillis"]:
            if (prev['epochMillis'] < epoch < curr['epochMillis']):
                df_click.at[i, 'scroll_distance'] = curr["scroll_top_distance"]
                break

    result = df_click.groupby('test_name')['scroll_distance'].sum()
    result.name = 'total_scroll_distance'
    return result

    result = df.groupby('test_name')['scroll_distance'].sum()
    result.name = 'total_scroll_distance'
    return result

def calc_clicks(df):
    clicks = df[df['type'] == 'click'].groupby('test_name').size()

    all_test_names = df['test_name'].unique()  
    clicks = clicks.reindex(all_test_names, fill_value=0)

    clicks.name = 'total_click_count'
    return clicks

def calc_inputs(df):
    df['data_length'] = df['data'].apply(lambda x: len(x) if x is not None else 0)

    inputs = df.groupby('test_name')['data_length'].sum()

    inputs.name = 'total_input_count'
    return inputs

def add_test_names(df):
    df.insert(0, 'test_name', '')
    pre_test_name = ''
    for index, row in df.iterrows():
        if (index == 0):
            pre_test_name = row['title'].split(":", 1)[1].strip()
        title = row['title']
        if (title is None):
            df.at[index, 'test_name'] = pre_test_name
        else:
            test_name = title.split(":", 1)[1].strip()
            df.at[index, 'test_name'] = test_name
            pre_test_name = test_name
    return df


def get_test_result(df):
    df['test_result'] = df['title'].str.extract(r'\((.*?)\)')
    result_df = df[['test_name', 'test_result']]
    return result_df

def filter_first_test_run(df):
    valid_indices = []
    for test_name, group in df.groupby('test_name'):
        group = group.sort_values('epochMillis')

        start_indices = group[group['title'].str.contains('Start', na=False)].index
        first_start_idx = start_indices[0]
        finish_condition = group.loc[first_start_idx:]['title'].str.contains('Finish', na=False)
        finish_indices = group.loc[first_start_idx:][finish_condition].index
        first_finish_idx = finish_indices[0]
        valid_range = group.loc[first_start_idx:first_finish_idx].index.tolist()
        valid_indices.extend(valid_range)
    return df.loc[valid_indices].sort_values('epochMillis').reset_index(drop=True)

def combine_test_data(db_path):
    df_click = get_mouseevent_from_db(db_path)
    df_input = get_inputevent_from_db(db_path)

    df_page_transition = get_page_transition_from_db(db_path)
    df_test_result = get_test_result_from_db(db_path)

    df_click = add_test_names(df_click)
    df_input = add_test_names(df_input)
    df_test_result = add_test_names(df_test_result)

    df_click = filter_first_test_run(df_click)
    df_input = filter_first_test_run(df_input)
    df_test_result = df_test_result.drop_duplicates(subset='test_name', keep='first')

    mouse_move_distances = calc_mouse_move_distances(df_click)
    mouse_scroll_distances = calc_mouse_scroll_distances(df_click, df_page_transition)
    click_counts = calc_clicks(df_click)
    inputs = calc_inputs(df_input)
    test_result = get_test_result(df_test_result)

    result = mouse_move_distances.to_frame().merge(
        mouse_scroll_distances.to_frame(),
        on='test_name',
        how='outer'  
    ).merge(
        click_counts.to_frame(),
        on='test_name',
        how='outer'
    ).merge(
        inputs.to_frame(),
        on='test_name',
        how='outer'
    ).merge(
        test_result,
        on='test_name',
        how='outer'
    )
    return result

def find_single_db_by_regex(pattern, base_dir):
    all_files = glob.glob(os.path.join(base_dir, "**", "*"), recursive=True)

    regex = re.compile(pattern)
    matched_files = [f for f in all_files if regex.search(os.path.basename(f))]

    if len(matched_files) == 1:
        db_path = matched_files[0]  
        return db_path
    elif len(matched_files) == 0:
        raise FileNotFoundError("No files found matching the regular expression.")
    else:
        raise RuntimeError(f"Found {len(matched_files)} matching files. Please refine the pattern to match exactly one file.")


def main(pattern, base_dir):
    try:
        db_path = find_single_db_by_regex(pattern+r"_.*\.db$", base_dir)
        result = combine_test_data(db_path)
        return result
    except Exception as e:
        print("error", e)

if __name__ == "__main__":
    main()