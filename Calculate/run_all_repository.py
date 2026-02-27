import calculate_operations
import os
import argparse

def main():
    parser = argparse.ArgumentParser(description="Calculate effort from logs.")
    
    parser.add_argument("--base_dir", type=str, required=True, help="Path to the directory containing the input database (.db) files")
    parser.add_argument("--output_dir", type=str, required=True, help="Path to the directory where the resulting CSV files will be saved")
    parser.add_argument("--start", type=int, required=True, help="Starting commit sequence number (e.g., 913)")
    parser.add_argument("--end", type=int, required=True, help="Ending commit sequence number (exclusive, e.g., 915)")
    
    args = parser.parse_args()

    os.makedirs(args.output_dir, exist_ok=True)
    
    for i in range(args.start, args.end):
        pattern = rf"{i}"
        print(f"\n--- {i} ---")
        try:
            result = calculate_operations.main(pattern, args.base_dir)
            result['Commit Number'] = i

            output_file = os.path.join(args.output_dir, f"{i}_result.csv")
            result.to_csv(output_file, index=False)  
            print(f"Result saved to {output_file}")
        except Exception as e:
            print(f"Error (Commit {i}): {e}")

if __name__ == "__main__":
    main()
