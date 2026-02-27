BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "CompositionEvent" (
	"compositioneventID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"data"	TEXT,
	"locale"	TEXT
);
CREATE TABLE IF NOT EXISTS "Document" (
	"documentID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"characterSet"	TEXT,
	"compatMode"	TEXT,
	"contentType"	TEXT,
	"cookie"	TEXT,
	"designMode"	TEXT,
	"dir"	TEXT,
	"documentURI"	TEXT,
	"domain"	TEXT,
	"hidden"	INTEGER,
	"lastModified"	TEXT,
	"readyState"	TEXT,
	"referrer"	TEXT,
	"selectedStyleSheetSet"	TEXT,
	"title"	TEXT,
	"URL"	TEXT,
	"visibilityState"	TEXT
);
CREATE TABLE IF NOT EXISTS "CharacterData" (
	"characterDataID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"data"	TEXT,
	"length"	INTEGER
);
CREATE TABLE IF NOT EXISTS "Text" (
	"textID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"wholeText"	TEXT
);
CREATE TABLE IF NOT EXISTS "Documenttype" (
	"documenttypeID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"name"	TEXT,
	"publicId"	TEXT,
	"systemId"	TEXT
);
CREATE TABLE IF NOT EXISTS "Element" (
	"elementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"className"	TEXT,
	"clientHeight"	INTEGER,
	"clientLeft"	INTEGER,
	"clientTop"	INTEGER,
	"clientWidth"	INTEGER,
	"computedName"	TEXT,
	"computedRole"	TEXT,
	"id"	TEXT,
	"innerHTML"	TEXT,
	"localName"	TEXT,
	"namespaceURI"	TEXT,
	"outerHTML"	TEXT,
	"prefix"	TEXT,
	"scrollHeight"	INTEGER,
	"scrollLeft"	INTEGER,
	"scrollTop"	INTEGER,
	"scrollWidth"	INTEGER,
	"selector"	TEXT,
	"slot"	TEXT,
	"tagName"	TEXT
);
CREATE TABLE IF NOT EXISTS "Event" (
	"eventID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"bubbles"	INTEGER,
	"cancelable"	INTEGER,
	"composed"	INTEGER,
	"defaultPrevented"	INTEGER,
	"eventPhase"	INTEGER,
	"timeStamp"	REAL,
	"epochMillis"	INTEGER,
	"type"	TEXT,
	"isTrusted"	INTEGER
);
CREATE TABLE IF NOT EXISTS "FocusEvent" (
	"focuseventID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER
);
CREATE TABLE IF NOT EXISTS "HTMLAnchorElement" (
	"htmlanchorelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"download"	TEXT,
	"hash"	TEXT,
	"host"	TEXT,
	"hostname"	TEXT,
	"href"	TEXT,
	"hreflang"	TEXT,
	"media"	TEXT,
	"origin"	TEXT,
	"password"	TEXT,
	"pathname"	TEXT,
	"port"	TEXT,
	"protocol"	TEXT,
	"refferrerPolicy"	TEXT,
	"rel"	TEXT,
	"search"	TEXT,
	"target"	TEXT,
	"text"	TEXT,
	"type"	TEXT,
	"username"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLAreaElement" (
	"htmlareaelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"alt"	TEXT,
	"coords"	TEXT,
	"download"	TEXT,
	"hash"	TEXT,
	"host"	TEXT,
	"hostname"	TEXT,
	"media"	TEXT,
	"origin"	TEXT,
	"password"	TEXT,
	"pathname"	TEXT,
	"port"	TEXT,
	"protocol"	TEXT,
	"refferrerPolicy"	TEXT,
	"rel"	TEXT,
	"search"	TEXT,
	"shape"	TEXT,
	"target"	TEXT,
	"type"	TEXT,
	"username"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLBaseElement" (
	"htmlbaseelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"href"	TEXT,
	"target"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLButtonElement" (
	"htmlbuttonelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"autofocus"	INTEGER,
	"disabled"	INTEGER,
	"formAcion"	TEXT,
	"formEnctype"	TEXT,
	"formMethod"	TEXT,
	"formNoValidate"	INTEGER,
	"formTarget"	TEXT,
	"name"	TEXT,
	"type"	TEXT,
	"validationMessage"	TEXT,
	"value"	TEXT,
	"willValidate"	INTEGER
);
CREATE TABLE IF NOT EXISTS "HTMLCanvasElement" (
	"htmlcanvaselementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"height"	INTEGER,
	"width"	INTEGER
);
CREATE TABLE IF NOT EXISTS "HTMLDataElement" (
	"htmldataelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"value"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLDialogElement" (
	"htmldialogelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"open"	INTEGER,
	"returnValue"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLElement" (
	"htmlelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"accessKey"	TEXT,
	"accessKeyLabel"	TEXT,
	"contentEditable"	TEXT,
	"draggable"	INTEGER,
	"hidden"	INTEGER,
	"isContentEditable"	INTEGER,
	"itemScope"	INTEGER,
	"itemId"	TEXT,
	"lang"	TEXT,
	"offsetHeight"	INTEGER,
	"offsetLeft"	INTEGER,
	"offsetTop"	INTEGER,
	"offsetWidth"	INTEGER,
	"spellcheck"	INTEGER,
	"tabIndex"	INTEGER,
	"title"	TEXT,
	"translate"	INTEGER
);
CREATE TABLE IF NOT EXISTS "HTMLEmbedElement" (
	"htmlembedelement"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"height"	TEXT,
	"src"	TEXT,
	"type"	TEXT,
	"width"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLFieldSetElement" (
	"htmlfieldelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"disabled"	INTEGER,
	"name"	TEXT,
	"type"	TEXT,
	"validationMessage"	TEXT,
	"willValidate"	INTEGER
);
CREATE TABLE IF NOT EXISTS "HTMLFormElement" (
	"htmlformelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"acceptCharset"	TEXT,
	"action"	TEXT,
	"autocomplete"	TEXT,
	"encoding"	TEXT,
	"enctype"	TEXT,
	"length"	INTEGER,
	"method"	TEXT,
	"name"	TEXT,
	"noValidate"	INTEGER,
	"target"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLIFrameElement" (
	"htmliframelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"allow"	TEXT,
	"allowFullscreen"	INTEGER,
	"allowPaymentRequest"	INTEGER,
	"height"	TEXT,
	"name"	TEXT,
	"referrerPolicy"	TEXT,
	"src"	TEXT,
	"srcdoc"	TEXT,
	"width"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLInputElement" (
	"htmlinputelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"accept"	TEXT,
	"alt"	TEXT,
	"autocapitalize"	TEXT,
	"autocomplete"	TEXT,
	"autofocus"	INTEGER,
	"checked"	INTEGER,
	"defaultChecked"	INTEGER,
	"defaultValue"	TEXT,
	"dirName"	TEXT,
	"disabled"	INTEGER,
	"formAction"	TEXT,
	"formEnctype"	TEXT,
	"formMethod"	TEXT,
	"formNoValidate"	TEXT,
	"formTarget"	TEXT,
	"height"	INTEGER,
	"indeterminate"	INTEGER,
	"max"	TEXT,
	"maxLength"	INTEGER,
	"min"	TEXT,
	"multiple"	INTEGER,
	"name"	TEXT,
	"pattern"	TEXT,
	"placeholder"	TEXT,
	"readyOnly"	INTEGER,
	"required"	INTEGER,
	"selectionDirection"	TEXT,
	"selectionEnd"	INTEGER,
	"selectionStart"	INTEGER,
	"size"	INTEGER,
	"src"	TEXT,
	"step"	TEXT,
	"type"	TEXT,
	"validationMessage"	TEXT,
	"value"	TEXT,
	"valueAsNumber"	REAL,
	"width"	INTEGER,
	"willValidate"	INTEGER
);
CREATE TABLE IF NOT EXISTS "HTMLLIElement" (
	"htmllielementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"value"	INTEGER
);
CREATE TABLE IF NOT EXISTS "HTMLLabelElement" (
	"htmllabelelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"htmlFor"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLLegendElement" (
	"htmllegendelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER
);
CREATE TABLE IF NOT EXISTS "HTMLLinkElement" (
	"htmllinkelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"as"	TEXT,
	"crossOrigin"	TEXT,
	"disabled"	INTEGER,
	"href"	TEXT,
	"hreflang"	TEXT,
	"media"	TEXT,
	"referrerPolicy"	TEXT,
	"rel"	TEXT,
	"type"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLMetaElement" (
	"htmlmetaelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"content"	TEXT,
	"httpEquiv"	TEXT,
	"name"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLMeterElement" (
	"htmlmeterelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"high"	REAL,
	"low"	REAL,
	"max"	REAL,
	"min"	REAL,
	"optimum"	REAL
);
CREATE TABLE IF NOT EXISTS "HTMLModElement" (
	"htmlmodelelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"cite"	TEXT,
	"detetime"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLOListElement" (
	"htmlolistelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"reversed"	INTEGER,
	"start"	INTEGER,
	"type"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLObjectElement" (
	"htmlobjectelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"data"	TEXT,
	"height"	TEXT,
	"name"	TEXT,
	"useMap"	TEXT,
	"validationMessage"	TEXT,
	"width"	TEXT,
	"willValidate"	INTEGER
);
CREATE TABLE IF NOT EXISTS "HTMLMapElement" (
	"mapelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"name"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLMediaElement" (
	"htmlmediaelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"autoplay"	INTEGER,
	"crossOrigin"	TEXT,
	"currentSrc"	TEXT,
	"currentTime"	REAL,
	"defaultMuted"	INTEGER,
	"defaultPlaybackRate"	REAL,
	"disableRemotePlayback"	INTEGER,
	"duration"	REAL,
	"ended"	INTEGER,
	"loop"	INTEGER,
	"mediaGroup"	TEXT,
	"muted"	INTEGER,
	"networkState"	INTEGER,
	"paused"	INTEGER,
	"playbackRate"	REAL,
	"preload"	TEXT,
	"readyState"	INTEGER,
	"seeking"	INTEGER,
	"sinkId"	TEXT,
	"src"	TEXT,
	"volume"	REAL
);
CREATE TABLE IF NOT EXISTS "HTMLOptGroupElement" (
	"htmloptelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"disabled"	INTEGER,
	"label"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLOptionElement" (
	"htmloptionelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"defaultSelected"	INTEGER,
	"disabled"	INTEGER,
	"index"	INTEGER,
	"label"	TEXT,
	"selected"	INTEGER,
	"text"	TEXT,
	"value"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLOutputElement" (
	"htmloutputelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"defaultValue"	TEXT,
	"name"	TEXT,
	"type"	TEXT,
	"validationMessage"	TEXT,
	"value"	TEXT,
	"willValidate"	INTEGER
);
CREATE TABLE IF NOT EXISTS "HTMLParamElement" (
	"htmlparamelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"name"	TEXT,
	"value"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLProgressElement" (
	"htmlprogresselementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"max"	REAL,
	"position"	REAL,
	"value"	REAL
);
CREATE TABLE IF NOT EXISTS "HTMLQuoteElement" (
	"htmlquoteelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"cite"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLScriptElement" (
	"htmlscriptelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"async"	INTEGER,
	"charset"	TEXT,
	"crossOrigin"	TEXT,
	"defer"	INTEGER,
	"noModule"	INTEGER,
	"src"	TEXT,
	"text"	TEXT,
	"type"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLSelectElement" (
	"htmlselectelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"autofocus"	INTEGER,
	"disabled"	INTEGER,
	"length"	INTEGER,
	"multiple"	INTEGER,
	"name"	TEXT,
	"required"	INTEGER,
	"selectedIndex"	INTEGER,
	"size"	INTEGER,
	"type"	TEXT,
	"validationMessage"	TEXT,
	"value"	TEXT,
	"willValidate"	INTEGER
);
CREATE TABLE IF NOT EXISTS "HTMLSlotElement" (
	"htmlslotelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"name"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLSourceElement" (
	"htmlsourceelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"keySystem"	TEXT,
	"media"	TEXT,
	"sizes"	TEXT,
	"src"	TEXT,
	"srcset"	TEXT,
	"type"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLStyleElement" (
	"htmlstyleelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"disabled"	INTEGER,
	"media"	TEXT,
	"type"	TEXT
	
);
CREATE TABLE IF NOT EXISTS "HTMLTableCellElement" (
	"htmltablecellelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"abbr"	TEXT,
	"cellIndex"	INTEGER,
	"colSpan"	INTEGER,
	"rowSpan"	INTEGER,
	"scope"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLTableColElement" (
	"htmltablecolelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"span"	INTEGER
);
CREATE TABLE IF NOT EXISTS "HTMLTableElement" (
	"htmltableelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER
);
CREATE TABLE IF NOT EXISTS "HTMLTableRowElement" (
	"htmltablerowelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"rowIndex"	INTEGER,
	"sectionRowIndex"	INTEGER
);
CREATE TABLE IF NOT EXISTS "HTMLTextAreaElement" (
	"htmltextareaelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"autocapitalize"	TEXT,
	"autocomplete"	TEXT,
	"autofocus"	INTEGER,
	"cols"	INTEGER,
	"defaultValue"	TEXT,
	"disabled"	INTEGER,
	"inputMode"	TEXT,
	"maxLength"	INTEGER,
	"name"	TEXT,
	"placeholder"	TEXT,
	"readOnly"	INTEGER,
	"required"	INTEGER,
	"rows"	INTEGER,
	"selectionDirection"	TEXT,
	"selectionEnd"	INTEGER,
	"selectionStart"	INTEGER,
	"textLength"	INTEGER,
	"type"	TEXT,
	"validationMessage"	TEXT,
	"value"	TEXT,
	"willValidate"	INTEGER,
	"wrap"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLTimeElement" (
	"htmltimeelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"dateTime"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLTitleElement" (
	"htmltitleelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"text"	TEXT
);
CREATE TABLE IF NOT EXISTS "HTMLTrackElement" (
	"htmltrackelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"kind"	TEXT,
	"label"	TEXT,
	"m_default"	INTEGER,
	"src"	TEXT,
	"srclang"	TEXT,
	"readyState"	INTEGER
);
CREATE TABLE IF NOT EXISTS "HTMLVideoElement" (
	"htmlvideoelementID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"poster"	TEXT,
	"videoHeight"	INTEGER,
	"videoWidth"	INTEGER,
	"width"	INTEGER
);
CREATE TABLE IF NOT EXISTS "InputEvent" (
	"inputeventID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"data"	TEXT,
	"inputType"	TEXT,
	"isComposing"	INTEGER
);
CREATE TABLE IF NOT EXISTS "TouchEvent" (
	"toucheventID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"altKey"	INTEGER,
	"ctrlKey"	INTEGER,
	"metaKey"	INTEGER,
	"shiftKey"	INTEGER
);
CREATE TABLE IF NOT EXISTS "KeyboardEvent" (
	"keyboardeventID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"altKey"	INTEGER,
	"code"	TEXT,
	"ctrlKey"	INTEGER,
	"isComposing"	INTEGER,
	"key2"	TEXT,
	"location"	INTEGER,
	"metaKey"	INTEGER,
	"repeat2"	INTEGER,
	"shiftKey"	INTEGER
);
CREATE TABLE IF NOT EXISTS "Log" (
	"logID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"EventType"	TEXT,
	"NodeType"	TEXT
);
CREATE TABLE IF NOT EXISTS "MouseEvent" (
	"mouseeventID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"altKey"	INTEGER,
	"button"	INTEGER,
	"buttons"	INTEGER,
	"client_X"	INTEGER,
	"client_Y"	INTEGER,
	"ctrlKey"	INTEGER,
	"metaKey"	INTEGER,
	"movementX"	INTEGER,
	"movementY"	INTEGER,
	"offsetX"	INTEGER,
	"offsetY"	INTEGER,
	"pageX"	INTEGER,
	"pageY"	INTEGER,
	"screenX"	INTEGER,
	"screenY"	INTEGER,
	"shiftKey"	INTEGER,
	"x"	INTEGER,
	"y"	INTEGER
);
CREATE TABLE IF NOT EXISTS "Node" (
	"nodeID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"baseURI"	TEXT,
	"innerText"	TEXT,
	"nodeName"	TEXT,
	"nodeValue"	TEXT,
	"textContent"	TEXT
);
CREATE TABLE IF NOT EXISTS "UIEvent" (
	"uieventID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"detail"	INTEGER
);
CREATE TABLE IF NOT EXISTS "WheelEvent" (
	"wheeleventID"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ref"	INTEGER,
	"deltaX"	REAL,
	"deltaY"	REAL,
	"deltaZ"	REAL,
	"dataMode"	INTEGER
);
COMMIT;
