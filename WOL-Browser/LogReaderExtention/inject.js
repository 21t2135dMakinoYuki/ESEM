// --- Event Listener Registration ---

let eventSpecs;

const eventSpecRequest = new XMLHttpRequest();
eventSpecRequest.open("get", "http://localhost:8080/OpLoRServerPrototype-1.0-SNAPSHOT/standard_event.json", true);
eventSpecRequest.send(null);
eventSpecRequest.onreadystatechange = () => {
    // Abort if the request failed or returned a non-200 status
    if (eventSpecRequest.readyState !== 4 || eventSpecRequest.status !== 200) {
        return;
    }
    eventSpecs = JSON.parse(eventSpecRequest.responseText);
    
    for (const eventSpec of eventSpecs) {
        if (isTarget(eventSpec)) {
			addEventListenerToAllEventTargets(document, eventSpec);
        }
    }

    // Start monitoring DOM changes for dynamic elements
    observeDOMChanges();
};

function isTarget(eventSpec) {
	return !eventSpec.deprecated    
        && !eventSpec.experimental  
        && !eventSpec.type.deprecated   
        && !eventSpec.type.experimental 
}


function addEventListenerToAllEventTargets(candidate, eventSpec) {
	if (!(candidate instanceof EventTarget) || !eventSpec) {
        return;
    }
    candidate.addEventListener(eventSpec.name, sendEventLog, false);    
    
    // Recursively add event listeners to child nodes
    if (candidate instanceof Node) {
        for (const child of candidate.childNodes) {
            addEventListenerToAllEventTargets(child, eventSpec);
        }
    }
}

function observeDOMChanges() {
    observer.observe(document.body, {
        childList: true,   
        subtree: true 
    });
}

const observer = new MutationObserver((mutations) => {
    for (const mutation of mutations) {
        if (mutation.type === 'childList') {
            // Add listeners to newly created DOM nodes
            for (const addedNode of mutation.addedNodes) {
                if (addedNode instanceof Node) {
                    for (const eventSpec of eventSpecs) {
                        if (isTarget(eventSpec)) {
                            addEventListenerToAllEventTargets(addedNode, eventSpec);
                        }
                    }
                }
            }
            for (const removedNode of mutation.removedNodes) {
                if (removedNode instanceof Node) {
                    removeEventListenersFromElement(removedNode);
                }
            }
        }
    }
});

function removeEventListenersFromElement(element) {
    if (!(element instanceof EventTarget)) {
        return;
    }
    for (const eventSpec of eventSpecs) {
        if (isTarget(eventSpec)) {
            element.removeEventListener(eventSpec.name, sendEventLog, false);
        }
    }
    if (element instanceof Node) {
        for (const child of element.childNodes) {
            removeEventListenersFromElement(child);
        }
    }
}


const ignoredEvents = [
    "mouseenter", "mouseover", "mouseout", "mouseleave", "dblclick",
    "mousedown", "mouseup", "mousemove",
    "pointerenter", "pointerover", "pointerout", "pointerleave", "pointerdown", "pointerup", "pointermove",
    "keyup", "keydown", "keypress",
    "change", "focus", "blur",
    "load"
];

function sendEventLog(event) {
    // Only capture logs for the element that originated the event (ignore bubbling)
    if(event.target !== event.currentTarget) {
        return;
    }

    // Filter out high-frequency or unnecessary events
    if(ignoredEvents.includes(event.type)){
        return;
    }

    const eventDate = new Date();  
    const json = [];

    let {EventType, json: EventLog} = parseEvent(event, eventDate);    
    let {NodeType, json: NodeLog} = parseElement(event.target); 
    
    json.push(EventLog);
    
    json.push(NodeLog);

    let oplorLog = [];

    oplorLog.push(customStringify(json));
    
    sendLog(oplorLog, EventType, NodeType);
}

function sendLog(oplorLog, EventType, NodeType) {
    if (oplorLog && oplorLog.length > 0){
        operationLogRequest = init()
		if(operationLogRequest) {
            operationLogRequest.send(EventType+"@@"+NodeType+"@@"+ oplorLog.toString());
        }
    }
}

function init(){
    let operationLogRequest = new XMLHttpRequest();
    
	operationLogRequest.open("post", "http://localhost:8080/OpLoRServerPrototype-1.0-SNAPSHOT/HW", true);//開発環境を経由しない場合
    
    operationLogRequest.setRequestHeader("Content-Type", "application/json; charset=ASCII");
    
    // Allow sending credentials in cross-origin (CORS) requests
    operationLogRequest.withCredentials=true;

    operationLogRequest.onreadystatechange = () => {
        if (operationLogRequest.readyState === 4) {
            if (operationLogRequest.status === 200) {
                console.log("Log sent successfully.");
            } else if (operationLogRequest.status === 429) {
                console.error("Too Many Requests. Please slow down.");
                return; 
            } else {
                console.error(`Failed to send log. Status code: ${operationLogRequest.status}`);
                return;
            }
        }
    }
    return operationLogRequest;
}

function parseEvent(event, eventDate) {
    let json = {};
    let EventType;
    if (typeof Event === 'function'&&event instanceof Event) {
        EventType='Event';
        json = Object.assign(json, createEventJson(event, eventDate));
    }
    if (typeof UIEvent === 'function'&&event instanceof UIEvent) {
        EventType='UIEvent';
        json = Object.assign(json, createUIEventJson(event));
    }
    if (typeof FocusEvent === 'function'&&event instanceof FocusEvent) {
        EventType='FocusEvent';
        json = Object.assign(json, createFocusEventJson(event));
    }
    if (typeof MouseEvent === 'function'&&event instanceof MouseEvent) {
        EventType='MouseEvent';
        json = Object.assign(json, createMouseEventJson(event));
    }
    if (typeof TouchEvent === 'function'&&event instanceof TouchEvent) {
        EventType='TouchEvent';
        json = Object.assign(json, createTouchEventJson(event));
    }
    if (typeof CompositionEvent === 'function'&&event instanceof CompositionEvent) {
        EventType='CompositionEvent';
        json = Object.assign(json, createCompositionEventJson(event));
    }
    if (typeof KeyboardEvent === 'function'&&event instanceof KeyboardEvent) {
        EventType='KeyboardEvent';
        json = Object.assign(json, createKeyboardEventJson(event));
    }
    if (typeof WheelEvent === 'function'&&event instanceof WheelEvent) {
        EventType='WheelEvent';
        json = Object.assign(json, createWheelEventJson(event));
    }
    /*
    if(typeof PointerEvent=='function'&&event instanceof PointerEvent){
        EventType='PointerEvent';
        json=Object.assign(json, createPointerEventJson(event));
    }
    */
    if (typeof InputEvent === 'function'&&event instanceof InputEvent) {
        EventType='InputEvent';
        json = Object.assign(json, createInputEventJson(event));
    }
    if (event.type === "selectionchange") {
        EventType='selectionchange';
        json = Object.assign(json, {
            anchorNode: getSelectorFromElement(getSelection().anchorNode).join(" > "),
            anchorOffset: getSelection().anchorOffset,
            focusNode: getSelectorFromElement(getSelection().focusNode).join(" > "),
            focusOffset: getSelection().focusOffset,
            isCollapsed: getSelection().isCollapsed,
            rangeCount: getSelection().rangeCount,
            type: getSelection().type
        });
    }
    return {EventType, json};
}

function createEventJson(event, eventDate) {
    return {
        bubbles: event.bubbles,
        cancelable: event.cancelable,
        composed: event.composed,
        //currentTarget: event.currentTarget,   
        defaultPrevented: event.defaultPrevented,
        eventPhase: event.eventPhase,
        timeStamp: event.timeStamp,
        epochMillis: eventDate.getTime(),
        type: event.type,
        isTrusted: event.isTrusted
    }
}
function createUIEventJson(uiEvent) {
    return {
        detail: uiEvent.detail,
        //view: uiEvent.view   
    }
}
function createPointerEventJson(pointerEvent) { 
    return {
    }
}
function createFocusEventJson(focusEvent) {
    return {
        //relatedTarget: focusEvent.relatedTarget   
    }
}
function createMouseEventJson(mouseEvent) {
    return {
        altKey: mouseEvent.altKey,
        button: mouseEvent.button,
        buttons: mouseEvent.buttons,
        clientX: mouseEvent.clientX,
        clientY: mouseEvent.clientY,
        ctrlKey: mouseEvent.ctrlKey,
        metaKey: mouseEvent.metaKey,
        movementX: mouseEvent.movementX,
        movementY: mouseEvent.movementY,
        offsetX: mouseEvent.offsetX,
        offsetY: mouseEvent.offsetY,
        pageX: mouseEvent.pageX,
        pageY: mouseEvent.pageY,
        //relatedTarget: mouseEvent.relatedTarget,
        screenX: mouseEvent.screenX,
        screenY: mouseEvent.screenY,
        shiftKey: mouseEvent.shiftKey,
        x: mouseEvent.x, //experimental
        y: mouseEvent.y //experimental
    }
}
function createTouchEventJson(event) {
    return {
        altKey: event.altKey,
        //changedTouches: event.changedTouches,
        ctrlKey: event.ctrlKey,
        metaKey: event.metaKey,
        shiftKey: event.shiftKey,
        //targetTouches: event.targetTouches, 
        //touches: event.touches    
    };
}
function createCompositionEventJson(compositionEvent) {
    return {
        data: composition.data,
        locale: composition.locale
    }
}
function createKeyboardEventJson(keyboardEvent) {
    return {
        altKey: keyboardEvent.altKey,
        code: keyboardEvent.code,
        ctrlKey: keyboardEvent.ctrlKey,
        isComposing: keyboardEvent.isComposing,
        key: keyboardEvent.key,
        location: keyboardEvent.location,
        metaKey: keyboardEvent.metaKey,
        repeat: keyboardEvent.repeat,
        shiftkey: keyboardEvent.shiftKey
    }
}
function createWheelEventJson(event) {
    return {
        deltaX: event.deltaX,
        deltaY: event.deltaY,
        deltaZ: event.deltaZ,
        deltaMode: event.deltaMode
    }
}
function createInputEventJson(event) {
    return {
        data: event.data,
        //dataTransfer: event.dataTransfer,
        inputType: event.inputType,
        isComposing: event.isComposing
    };
}


function parseElement(element) {
    let json = {};
    let NodeType
    if (typeof Node === 'function'&&element instanceof Node) {
        NodeType='Node';
        json = Object.assign(json, createNodeJson(element));
    }
    if (typeof Document === 'function'&&element instanceof Document) {
        NodeType='Document';
        json = Object.assign(json, createDocumentJson(element));
    }
    if (typeof Element === 'function'&&element instanceof Element) {
        NodeType='Element';
        json = Object.assign(json, createElementJson(element));
    }
    if (typeof CharacterData === 'function'&&element instanceof CharacterData) {
        NodeType='CharacterData';
        json = Object.assign(json, createCharacterDataJson(element));
    }
    if (typeof Text === 'function'&&element instanceof Text) {
        NodeType='Text';
        json = Object.assign(json, createTextJson(element));
    }
    /*  
    if (typeof DocumentFragment === 'function'&&element instanceof DocumentFragment) {
        NodeType='DocumentFragment';
        json = Object.assign(json, createDocumentFragmentJson(element));
    }
    */
    if (typeof DocumentType === 'function'&&element instanceof DocumentType) {
        NodeType='DocumentType';
        json = Object.assign(json, createDocumentTypeJson(element));
    }
    if (typeof HTMLElement === 'function'&&element instanceof HTMLElement) {
        NodeType='HTMLElement';
        json = Object.assign(json, createHTMLElementJson(element));
    }
    /*  
    if (typeof SVGElement === 'function'&&element instanceof SVGElement) {
        NodeType='SVGElement';
        json = Object.assign(json, createSVGElementJson(element));
    }
    */
    if (typeof HTMLAbchorElement === 'function'&&element instanceof HTMLAnchorElement) {
        NodeType='HTMLAnchorElement';
        json = Object.assign(json, createHTMLAnchorElementJson(element));
    }
    if (typeof HTMLAreaElement === 'function'&&element instanceof HTMLAreaElement) {
        NodeType='HTMLAreaElement';
        json = Object.assign(json, createHTMLAreaElementJson(element));
    }
    if (typeof HTMLBaseElement === 'function'&&element instanceof HTMLBaseElement) {
        NodeType='HTMLBaseElement';
        json = Object.assign(json, createHTMLBaseElementJson(element));
    }
    if (typeof HTMLButtonElement === 'function'&&element instanceof HTMLButtonElement) {
        NodeType='HTMLButtonElement';
        json = Object.assign(json, createHTMLButtonElementJson(element));
    }
    if (typeof HTMLCanvasElement === 'function'&&element instanceof HTMLCanvasElement) {
        NodeType='HTMLCanvasElement';
        json = Object.assign(json, createHTMLCanvasElementJson(element));
    }
    if (typeof HTMLDataElement === 'function'&&element instanceof HTMLDataElement) {
        NodeType='HTMLDataElement';
        json = Object.assign(json, createHTMLDataElementJson(element));
    }
    /* 
    if (typeof HTMLDataListElement === 'function'&&element instanceof HTMLDataListElement) {
        NodeType='HTMLDataListElement';
        json = Object.assign(json, createHTMLDataListElementJson(element));
    }
    */
    if (typeof HTMLDialogElement === 'function'&&element instanceof HTMLDialogElement) {
        NodeType='HTMLDialogElement';
        json = Object.assign(json, createHTMLDialogElementJson(element));
    }
    if (typeof HTMLEmbedElement === 'function'&&element instanceof HTMLEmbedElement) {
        NodeType='HTMLEmbedElement';
        json = Object.assign(json, createHTMLEmbedElementJson(element));
    }
    if (typeof HTMLFieldSetElement === 'function'&&element instanceof HTMLFieldSetElement) {
        NodeType='HTMLFieldSetElement';
        json = Object.assign(json, createHTMLFieldSetElementJson(element));
    }
    if (typeof HTMLFormElement === 'function'&&element instanceof HTMLFormElement) {
        NodeType='HTMLFormElement';
        json = Object.assign(json, createHTMLFormElementJson(element));
    }
    if (typeof HTMLIFrameElement === 'function'&&element instanceof HTMLIFrameElement) {
        NodeType='HTMLIFrameFormElement';
        json = Object.assign(json, createHTMLIFrameElementJson(element));
    }
    if (typeof HTMLInputElement === 'function'&&element instanceof HTMLInputElement) {
        NodeType='HTMLInputElement';
        json = Object.assign(json, createHTMLInputElementJson(element));
    }
    if (typeof HTMLLIElement === 'function'&&element instanceof HTMLLIElement) {
        NodeType='HTMLLIElement';
        json = Object.assign(json, createHTMLLIElementJson(element));
    }
    if (typeof HTMLLabelElement === 'function'&&element instanceof HTMLLabelElement) {
        NodeType='HTMLLabelElement';
        json = Object.assign(json, createHTMLLabelElementJson(element));
    }
    if (typeof HTMLLegendElement === 'function'&&element instanceof HTMLLegendElement) {
        NodeType='HTMLLegend';
        json = Object.assign(json, createHTMLLegendElementJson(element));
    }
    if (typeof HTMLLinkElement === 'function'&&element instanceof HTMLLinkElement) {
        NodeType='HTMLLinkElement';
        json = Object.assign(json, createHTMLLinkElementJson(element));
    }
    if (typeof HTMLMapElement === 'function'&&element instanceof HTMLMapElement) {
        NodeType='HTMLMapElement';
        json = Object.assign(json, createHTMLMapElementJson(element));
    }
    if (typeof HTMLMediaElement === 'function'&&element instanceof HTMLMediaElement) {
        NodeType='HTMLMediaElement';
        json = Object.assign(json, createHTMLMediaElementJson(element));
    }
    if (typeof HTMLMetaElement === 'function'&&element instanceof HTMLMetaElement) {
        NodeType='HTMLMetaElement';
        json = Object.assign(json, createHTMLMetaElementJson(element));
    }
    if (typeof HTMLMeterElement === 'function'&&element instanceof HTMLMeterElement) {
        NodeType='HTMLMeterElement';
        json = Object.assign(json, createHTMLMeterElementJson(element));
    }
    if (typeof HTMLModElement === 'function'&&element instanceof HTMLModElement) {
        NodeType='HTMLModElement';
        json = Object.assign(json, createHTMLModElementJson(element));
    }
    if (typeof HTMLOListElement === 'function'&&element instanceof HTMLOListElement) {
        NodeType='HTMLOListElement';
        json = Object.assign(json, createHTMLOListElementJson(element));
    }
    if (typeof HTMLObjectElement === 'function'&&element instanceof HTMLObjectElement) {
        NodeType='HTMLPbjectElement';
        json = Object.assign(json, createHTMLObjectElementJson(element));
    }
    if (typeof HTMLOptGroupElement === 'function'&&element instanceof HTMLOptGroupElement) {
        NodeType='HTMLOptGroupElement';
        json = Object.assign(json, createHTMLOptGroupElementJson(element));
    }
    if (typeof HTMLOptionElement === 'function'&&element instanceof HTMLOptionElement) {
        NodeType='HTMLOptionElement';
        json = Object.assign(json, createHTMLOptionElementJson(element));
    }
    if (typeof HTMLOutputElement === 'function'&&element instanceof HTMLOutputElement) {
        NodeType='HTMLOutputElement';
        json = Object.assign(json, createHTMLOutputElementJson(element));
    }
    if (typeof HTMLParamElement === 'function'&&element instanceof HTMLParamElement) {
        NodeType='HTMLParamElement';
        json = Object.assign(json, createHTMLParamElementJson(element));
    }
    if (typeof HTMLProgressElement === 'function'&&element instanceof HTMLProgressElement) {
        NodeType='HTMLProgressElement';
        json = Object.assign(json, createHTMLProgressElementJson(element));
    }
    if (typeof HTMLQuoteElement === 'function'&&element instanceof HTMLQuoteElement) {
        NodeType='HTMLQuoteElement';
        json = Object.assign(json, createHTMLQuoteElementJson(element));
    }
    if (typeof HTMLScriptElement === 'function'&&element instanceof HTMLScriptElement) {
        NodeType='HTMLScriptElement';
        json = Object.assign(json, createHTMLScriptElementJson(element));
    }
    if (typeof HTMLSelectElement === 'function'&&element instanceof HTMLSelectElement) {
        NodeType='HTMLSelectElement';
        json = Object.assign(json, createHTMLSelectElementJson(element));
    }
    if (typeof HTMLSlotElement === 'function'&&element instanceof HTMLSlotElement) {
        NodeType='HTMLSlotElement';
        json = Object.assign(json, createHTMLSlotElementJson(element));
    }
    if (typeof HTMLSourceElement === 'function'&&element instanceof HTMLSourceElement) {
        NodeType='HTMLSourceElement';
        json = Object.assign(json, createHTMLSourceElementJson(element));
    }
    if (typeof HTMLStyleElement === 'function'&&element instanceof HTMLStyleElement) {
        NodeType='HTMLStyleElement';
        json = Object.assign(json, createHTMLStyleElementJson(element));
    }
    if (typeof HTMLTableCellElement === 'function'&&element instanceof HTMLTableCellElement) {
        NodeType='HTMLTableCellElement';
        json = Object.assign(json, createHTMLTableCellElementJson(element));
    }
    if (typeof HTMLTableColElement === 'function'&&element instanceof HTMLTableColElement) {
        NodeType='HTMLTableColElement';
        json = Object.assign(json, createHTMLTableColElementJson(element));
    }
    if (typeof HTMLTableElement === 'function'&&element instanceof HTMLTableElement) {
        NodeType='HTMLTableElement';
        json = Object.assign(json, createHTMLTableElementJson(element));
    }
    if (typeof HTMLTableRowElement === 'function'&&element instanceof HTMLTableRowElement) {
        NodeType='HTMLTableRowElement';
        json = Object.assign(json, createHTMLTableRowElementJson(element));
    }
    /* 
    if (typeof HTMLTableSectionElement === 'function'&&element instanceof HTMLTableSectionElement) {
        NodeType='HTMLTableSectionElement';
        json = Object.assign(json, createHTMLTableSectionElementJson(element));
    }
    */
   /* 
    if (typeof HTMLTemplateElement === 'function'&&element instanceof HTMLTemplateElement) {
        NodeType='HTMLTemplateElement';
        json = Object.assign(json, createHTMLTemplateElementJson(element));
    }
    */
    if (typeof HTMLTextAreaElement === 'function'&&element instanceof HTMLTextAreaElement) {
        NodeType='HTMLTextAreaElement';
        json = Object.assign(json, createHTMLTextAreaElementJson(element));
    }
    if (typeof HTMLTimeElement === 'function'&&element instanceof HTMLTimeElement) {
        NodeType='HTMLTimeElement';
        json = Object.assign(json, createHTMLTimeElementJson(element));
    }
    if (typeof HTMLTitleElement === 'function'&&element instanceof HTMLTitleElement) {
        NodeType='HTMLTitleElement';
        json = Object.assign(json, createHTMLTitleElementJson(element));
    }
    if (typeof HTMLTrackElement === 'function'&&element instanceof HTMLTrackElement) {
        NodeType='HTMLTrackElement';
        json = Object.assign(json, createHTMLTrackElementJson(element));
    }
    if (typeof HTMLVideoElement === 'function'&&element instanceof HTMLVideoElement) {
        NodeType='HTMLVideoElement';
        json = Object.assign(json, createHTMLVideoElementJson(element));
    }
    return {NodeType, json};
}


function createNodeJson(node) {
    return {
        baseURI: node.baseURI,
        //childNodes: node.childNodes,  
        //firstChild: node.firstChild,  
        innerText: node.innerText,
        //lastChild: node.lastChild,   
        //nextSibling: node.nextSibling,    
        nodeName: node.nodeName,
        //nodeType: node.nodeType,
        nodeValue: node.nodeValue,
        //ownerDocument: node.ownerDocument,  
        //parentNode: node.parentNode,  
        //parentElement: node.parentElement,    
        //previousSibling: node.previousSibling,    
		textContent: node.textContent
    };
}
function createDocumentJson(document) {
    return {
        characterSet: document.characterSet,
        compatMode: document.compatMode,
        contentType: document.contentType,
        //doctype: document.doctype,    
        //documentElement: document.documentElement,   
        //documentURI: document.documentURI,   
        hidden: document.hidden,
        //implementation: document.implementation, 
        //lastStyleSheetSet: document.lastStyleSheetSet,    
        //pointerLockElement: document.pointerLockElement, 
        //preferredStyleSheetSet: document.preferredStyleSheetSet,  
        //scrollingElement: document.scrollingElement,  
        selectedStyleSheetSet: document.selectedStyleSheetSet,
        //styleSheets: document.styleSheets,  
        //styleSheetSets: document.styleSheetSets,  
        //timeline: document.timeline, 
        //undoManager: document.undoManager,    
        visibilityState: document.visibilityState,
        //children: document.children,
        //firstElementChild: document.firstElementChild, 
        //lastElementChild: document.lastElementChild, 
        //childElementCount: document.childElementCount, 
        //activeElement: document.activeElement,    
        //activeElementSelector: getSelectorFromElement(document.activeElement), 
        //anchors: document.anchors, 
        //body: document.body,  
        cookie: document.cookie,
        //defaultView: document.defaultView,   
        designMode: document.designMode,
        dir: document.dir,
        domain: document.domain,
        //embeds: document.embeds,  
        //forms: document.forms,    
        //head: document.head,  
        //images: document.images, 
        lastModified: document.lastModified,
        //links: document.links,   
        //location: document.location, 
        //plugins: document.plugins,    
        readyState: document.readyState,
        referrer: document.referrer,
        //scripts: document.scripts,   
        title: document.title,
        URL: document.URL,
    };
}
function createElementJson(element) {
    return {
        selector: getSelectorFromElement(element).join(" > "),
        //assignedSlot: element.assignedSlot, 
        //attributes: element.attributes,  
        //classList: element.classList, 
        className: element.className,
        clientHeight: element.clientHeight,
        clientLeft: element.clientLeft,
        clientTop: element.clientTop,
        clientWidth: element.clientWidth,
        computedName: element.computedName,
        computedRole: element.computedRole,
        id: element.id,
        innerHTML: element.innerHTML,
        localName: element.localName,
        namespaceURI: element.namespaceURI,
        //nextElementSibling: element.nextElementSibling,
        outerHTML: element.outerHTML,
        prefix: element.prefix,
        //previousElementSibling: element.previousElementSibling,   
        scrollHeight: element.scrollHeight,
        scrollLeft: element.scrollLeft,
        scrollTop: element.scrollTop,
        scrollWidth: element.scrollWidth,
        //shadowRoot: element.shadowRoot,   
        slot: element.slot,
        tagName: element.tagName,
        //undoManager: element.undoManager,
        //undoScope: element.undoScope    
    };
}
function createCharacterDataJson(characterData) {
    return {
        data: characterData.data,
        length: characterData.length,
        //nextElementSibling: characterData.nextElementSibling, 
        //previousElementSibling: characterData.previousElementSibling  
    };
}
function createTextJson(text) {
    return {
        wholeText: text.wholeText,
        //assignedSlot: text.assignedSlot  
    };
}
function createDocumentFragmentJson(documentFragment) {
    return {
        children: documentFragment.children,
        //firstElementChild: documentFragment.firstElementChild,   
        //lastElementChild: documentFragment.lastElementChild,  
        childElementCount: documentFragment.childElementCount
    };
}
function createDocumentTypeJson(documentType) {
    return {
        name: documentType.name,
        publicId: documentType.publicId,
        systemId: documentType.systemId
    };
}
function createHTMLElementJson(htmlElement) {
    return {
        accessKey: htmlElement.accessKey,
        accessKeyLabel: htmlElement.accessKeyLabel,
        contentEditable: htmlElement.contentEditable,
        isContentEditable: htmlElement.isContentEditable,
        //contextMenu: htmlElement.contextMenu, 
        //dataset: htmlElement.dataset, 
        //dir: htmlElement.dir, 
        draggable: htmlElement.draggable,
        //dropzone: htmlElement.dropzone,   dropzone: htmlElement.dropzone,
        hidden: htmlElement.hidden,
        itemScope: htmlElement.itemScope, //experimental
        //itemType: htmlElement.itemType,   
        itemId: htmlElement.itemId, //experimental
        //itemRef: htmlElement.itemRef, 
        //itemProp: htmlElement.itemProp,   
        //itemValue: htmlElement.itemValue,
        lang: htmlElement.lang,
        offsetHeight: htmlElement.offsetHeight,
        offsetLeft: htmlElement.offsetLeft,
        //offsetParent: htmlElement.offsetParent,   
        offsetTop: htmlElement.offsetTop,
        offsetWidth: htmlElement.offsetWidth,
        //properties: htmlElement.properties,   
        spellcheck: htmlElement.spellcheck,
        //style: htmlElement.style, 
        tabIndex: htmlElement.tabIndex,
		title: htmlElement.title,
        translate: htmlElement.translate
    };
}
function createSVGElementJson(svgElement) {
    return {
        dataset: svgElement.dataset,
        id: svgElement.id
        //ownerSVGElement: svgElement.ownerSVGElement
    };
}
function createHTMLAnchorElementJson(htmlAnchorElement) {
    return {
        download: htmlAnchorElement.download,
        hash: htmlAnchorElement.hash,
        host: htmlAnchorElement.host,
        hostname: htmlAnchorElement.hostname,
        href: htmlAnchorElement.href,
        hreflang: htmlAnchorElement.hreflang,
        media: htmlAnchorElement.media,
        password: htmlAnchorElement.password,
        origin: htmlAnchorElement.origin,
        pathname: htmlAnchorElement.pathname,
        port: htmlAnchorElement.port,
        protocol: htmlAnchorElement.protocol,
        referrerPolicy: htmlAnchorElement.referrerPolicy,
        rel: htmlAnchorElement.rel,
        //relList: htmlAnchorElement.relList,  
        search: htmlAnchorElement.search,
        target: htmlAnchorElement.target,
        text: htmlAnchorElement.text,
        type: htmlAnchorElement.type,
        username: htmlAnchorElement.username
    };
}
function createHTMLAreaElementJson(htmlAreaElement) {
    return {
        alt: htmlAreaElement.alt,
        coords: htmlAreaElement.coords,
        download: htmlAreaElement.download,
        hash: htmlAreaElement.hash,
        host: htmlAreaElement.host,
        hostname: htmlAreaElement.hostname,
        //href: htmlAreaElement.href,  
        //hreflang: htmlAreaElement.hreflang,   
        media: htmlAreaElement.media,
        password: htmlAreaElement.password,
        origin: htmlAreaElement.origin,
        pathname: htmlAreaElement.pathname,
        port: htmlAreaElement.port,
        protocol: htmlAreaElement.protocol,
        referrerPolicy: htmlAreaElement.referrerPolicy, //experimental
        rel: htmlAreaElement.rel,
        //relList: htmlAreaElement.relList, 
        search: htmlAreaElement.search,
        shape: htmlAreaElement.shape,
        target: htmlAreaElement.target,
        type: htmlAreaElement.type,
        username: htmlAreaElement.username
    };
}
function createHTMLBaseElementJson(htmlBaseElement) {
    return {
        href: htmlBaseElement.href,
        target: htmlBaseElement.target
    };
}
function createHTMLButtonElementJson(htmlButtonElement) {
    return {
        autofocus: htmlButtonElement.autofocus,
        disabled: htmlButtonElement.disabled,
        //form: htmlButtonElement.form, 
        //formSelector: getSelectorFromElement(htmlButtonElement.form).join(" > "),
        formAction: htmlButtonElement.formAction,
        formEnctype: htmlButtonElement.formEnctype,
        formMethod: htmlButtonElement.formMethod,
        formNoValidate: htmlButtonElement.formNoValidate,
        formTarget: htmlButtonElement.formTarget,
        //labels: htmlButtonElement.labels, 
        //menu: htmlButtonElement.menu, 
        name: htmlButtonElement.name,
        type: htmlButtonElement.type,
        validationMessage: htmlButtonElement.validationMessage,
        //validity: htmlButtonElement.validity, 
        value: htmlButtonElement.value,
        willValidate: htmlButtonElement.willValidate
    };
}
function createHTMLCanvasElementJson(htmlCanvasElement) {
    return {
        height: htmlCanvasElement.height,
        width: htmlCanvasElement.width
    };
}
function createHTMLDataElementJson(htmlDataElement) {
    return {
        value: htmlDataElement.value
    };
}
function createHTMLDataListElementJson(htmlDataListElement) {
    return {
        options: htmlDataListElement.options
    };
}
function createHTMLDialogElementJson(htmlDialogElement) {
    return {
        open: htmlDialogElement.open,
        returnValue: htmlDialogElement.returnValuef
    };
}
function createHTMLEmbedElementJson(htmlEmbedElement) {
    return {
        height: htmlEmbedElement.height,
        src: htmlEmbedElement.src,
        type: htmlEmbedElement.type,
        width: htmlEmbedElement.width
    };
}
function createHTMLFieldSetElementJson(htmlFieldSetElement) {
    return {
        disabled: htmlFieldSetElement.disabled,
        //elements: htmlFieldSetElement.elements, 
        //form: htmlFieldSetElement.form,   
        //formSelector: getSelectorFromElement(htmlFieldSetElement.form).join(" > "),  
        name: htmlFieldSetElement.name,
        type: htmlFieldSetElement.type,
        validationMessage: htmlFieldSetElement.validationMessage,
        //validity: htmlFieldSetElement.validity,  
        willValidate: htmlFieldSetElement.willValidate
    };
}
function createHTMLFormElementJson(htmlFormElement) {
    return {
        //elements: htmlFormElement.elements,  
        length: htmlFormElement.length,
        //name: htmlFormElement.name,  
        method: htmlFormElement.method,
        target: htmlFormElement.target,
        action: htmlFormElement.action,
        encoding: htmlFormElement.encoding,
        enctype: htmlFormElement.enctype,
        acceptCharset: htmlFormElement.acceptCharset,
        autocomplete: htmlFormElement.autocomplete,
        noValidate: htmlFormElement.noValidate
    };
}
function createHTMLIFrameElementJson(htmlIFrameElement) {
    return {
        allow: htmlIFrameElement.allow,
        allowFullscreen: htmlIFrameElement.allowFullscreen,
        allowPaymentRequest: htmlIFrameElement.allowPaymentRequest,
        //contentDocument: htmlIFrameElement.contentDocument,  
        //contentWindow: htmlIFrameElement.contentWindow,  
        height: htmlIFrameElement.height,
        name: htmlIFrameElement.name,
        referrerPolicy: htmlIFrameElement.referrerPolicy,
        //sandbox: htmlIFrameElement.sandbox, 
        src: htmlIFrameElement.src,
        srcdoc: htmlIFrameElement.srcdoc,
        width: htmlIFrameElement.width
    };
}
function createHTMLInputElementJson(htmlInputElement) {
   return {
        //form: htmlInputElement.form,  
        //formSelector: getSelectorFromElement(htmlInputElement.form).join(" > "),
        formAction: htmlInputElement.formAction,
        formEncType: htmlInputElement.formEncType,
        formMethod: htmlInputElement.formMethod,
        formNoValidate: htmlInputElement.formNoValidate,
        formTarget: htmlInputElement.formTarget,
        name: htmlInputElement.name,
        type: htmlInputElement.type,
        disabled: htmlInputElement.disabled,
        autofocus: htmlInputElement.autofocus,
        required: htmlInputElement.required,
        value: htmlInputElement.value,
        //validity: htmlInputElement.validity,  
        validationMessage: htmlInputElement.validationMessage,
        willValidate: htmlInputElement.willValidate,
        checked: htmlInputElement.checked,
        defaultChecked: htmlInputElement.defaultChecked,
        indeterminate: htmlInputElement.indeterminate,
        alt: htmlInputElement.alt,
        height: htmlInputElement.height,
        src: htmlInputElement.src,
        width: htmlInputElement.width,
        accept: htmlInputElement.accept,
        //files: htmlInputElement.files,   
        autocomplete: htmlInputElement.autocomplete,
        maxLength: htmlInputElement.maxLength,
        size: htmlInputElement.size,
        pattern: htmlInputElement.pattern,
        placeholder: htmlInputElement.placeholder,
        readOnly: htmlInputElement.readOnly,
        min: htmlInputElement.min,
        max: htmlInputElement.max,
        selectionStart: htmlInputElement.selectionStart,
        selectionEnd: htmlInputElement.selectionEnd,
        selectionDirection: htmlInputElement.selectionDirection,
        defaultValue: htmlInputElement.defaultValue,
        dirName: htmlInputElement.dirName,
        //list: htmlInputElement.list,  
        multiple: htmlInputElement.multiple,
        //labels: htmlInputElement.labels,  
        step: htmlInputElement.step,
        //valueAsDate: htmlInputElement.valueAsDate,   
        valueAsNumber: htmlInputElement.valueAsNumber,
        autocapitalize: htmlInputElement.autocapitalize
    };
}
function createHTMLLIElementJson(htmlLiElement) {
    return {
        value: htmlLiElement.value
    };
}
function createHTMLLabelElementJson(htmlLabelElement) {
    return {
        //control: htmlLabelElement.control,  
        //form: htmlLabelElement.form, 
        //formSelector: getSelectorFromElement(htmlLabelElement.form).join(" > "),  
        htmlFor: htmlLabelElement.htmlFor,
    };
}
function createHTMLLegendElementJson(htmlLegendElement) {
    return {
        //form: htmlLegendElement.form,
        //formSelector: getSelectorFromElement(htmlLegendElement.form).join(" > "), 
    };
}
function createHTMLLinkElementJson(htmlLinkElement) {
    return {
        as: htmlLinkElement.as,
        crossOrigin: htmlLinkElement.crossOrigin,
        disabled: htmlLinkElement.disabled,
        href: htmlLinkElement.href,
        hreflang: htmlLinkElement.hreflang,
        media: htmlLinkElement.media,
        referrerPolicy: htmlLinkElement.referrerPolicy,
        rel: htmlLinkElement.rel,
        //relList: htmlLinkElement.relList, 
        //sizes: htmlLinkElement.sizes, 
        //sheet: htmlLinkElement.sheet, 
        type: htmlLinkElement.type
    };
}
function createHTMLMapElementJson(htmlMapElement) {
    return {
        name: htmlMapElement.name,
        //areas: htmlMapElement.areas,  
        //images: htmlMapElement.images 
    };
}
function createHTMLMediaElementJson(htmlMediaElement) {
    return {
        //audioTracks: htmlMediaElement.audioTracks,    
        autoplay: htmlMediaElement.autoplay,
        //buffered: htmlMediaElement.buffered,  
        //controller: htmlMediaElement.controller,  
        //controls: htmlMediaElement.controls,  
        //controlsList: htmlMediaElement.controlsList, 
        crossOrigin: htmlMediaElement.crossOrigin,
        currentSrc: htmlMediaElement.currentSrc,
        currentTime: htmlMediaElement.currentTime,
        defaultMuted: htmlMediaElement.defaultMuted,
        defaultPlaybackRate: htmlMediaElement.defaultPlaybackRate,
        disableRemotePlayback: htmlMediaElement.disableRemotePlayback,
        duration: htmlMediaElement.duration,
        ended: htmlMediaElement.ended,
        //error: htmlMediaElement.error,    
        loop: htmlMediaElement.loop,
        mediaGroup: htmlMediaElement.mediaGroup,
        //mediaKeys: htmlMediaElement.mediaKeys,    
        muted: htmlMediaElement.muted,
        networkState: htmlMediaElement.networkState,
        paused: htmlMediaElement.paused,
        playbackRate: htmlMediaElement.playbackRate,
        //played: htmlMediaElement.played,
        preload: htmlMediaElement.preload,
        readyState: htmlMediaElement.readyState,
        //seekable: htmlMediaElement.seekable,  
        seeking: htmlMediaElement.seeking,
        sinkId: htmlMediaElement.sinkId,
        src: htmlMediaElement.src,
        //srcObject: htmlMediaElement.srcObject,    
        //textTracks: htmlMediaElement.textTracks,  
        //videoTracks: htmlMediaElement.videoTracks,   
        volume: htmlMediaElement.volume
    };
}
function createHTMLMetaElementJson(htmlMetaElement) {
    return {
        content: htmlMetaElement.content,
        httpEquiv: htmlMetaElement.httpEquiv,
        name: htmlMetaElement.name
    };
}
function createHTMLMeterElementJson(htmlMeterElement) {
    return {
        high: htmlMeterElement.high,
        low: htmlMeterElement.low,
        max: htmlMeterElement.max,
        min: htmlMeterElement.min,
        optimum: htmlMeterElement.optimum,
        //labels: htmlMeterElement.labels  
    };
}
function createHTMLModElementJson(htmlModElement) {
    return {
        cite: htmlModElement.cite,
        datetime: htmlModElement.datetime
    };
}
function createHTMLOListElementJson(htmlOListElement) {
    return {
        reversed: htmlOListElement.reversed,
        start: htmlOListElement.start,
        type: htmlOListElement.type
    };
}
function createHTMLObjectElementJson(htmlObjectElement) {
    return {
        //contentDocument: htmlObjectElement.contentDocument,   
        //contentWindow: htmlObjectElement.contentWindow,  
        data: htmlObjectElement.data,
        //form: htmlObjectElement.form, 
        //formSelector: getSelectorFromElement(htmlObjectElement.form).join(" > "),
        height: htmlObjectElement.height,
        name: htmlObjectElement.name,
        useMap: htmlObjectElement.useMap,
        validationMessage: htmlObjectElement.validationMessage,
        //validity: htmlObjectElement.validity, 
        width: htmlObjectElement.width,
        willValidate: htmlObjectElement.willValidate
    };
}
function createHTMLOptGroupElementJson(htmlOptGroupElement) {
    return {
        disabled: htmlOptGroupElement.disabled,
        label: htmlOptGroupElement.label
    };
}
function createHTMLOptionElementJson(htmlOptionElement) {
    return {
        defaultSelected: htmlOptionElement.defaultSelected,
        disabled: htmlOptionElement.disabled,
        //form: htmlOptionElement.form, 
        //formSelector: getSelectorFromElement(htmlOptionElement.form).join(" > "), 
        index: htmlOptionElement.index,
        label: htmlOptionElement.label,
        selected: htmlOptionElement.selected,
        text: htmlOptionElement.text,
        value: htmlOptionElement.value
    };
}
function createHTMLOutputElementJson(htmlOutputElement) {
    return {
        defaultValue: htmlOutputElement.defaultValue,
        //form: htmlOutputElement.form, 
        //formSelector: getSelectorFromElement(htmlOutputElement.form).join(" > "),
        //htmlFor: htmlOutputElement.htmlFor,   
        //labels: htmlOutputElement.labels,
        name: htmlOutputElement.name,
        type: htmlOutputElement.type,
        validationMessage: htmlOutputElement.validationMessage,
        //validity: htmlOutputElement.validity, 
        value: htmlOutputElement.value,
        willValidate: htmlOutputElement.willValidate
    };
}
function createHTMLParamElementJson(htmlParamElement) {
    return {
        name: htmlParamElement.name,
        value: htmlParamElement.value
    };
}
function createHTMLProgressElementJson(htmlProgressElement) {
    return {
        max: htmlProgressElement.max,
        position: htmlProgressElement.position,
        value: htmlProgressElement.value,
        //labels: htmlProgressElement.labels    
    };
}
function createHTMLQuoteElementJson(htmlQuoteElement) {
    return {
        cite: htmlQuoteElement.cite
    };
}
function createHTMLScriptElementJson(htmlScriptElement) {
    return {
        type: htmlScriptElement.type,
        src: htmlScriptElement.src,
        charset: htmlScriptElement.charset,
        async: htmlScriptElement.async,
        defer: htmlScriptElement.defer,
        crossOrigin: htmlScriptElement.crossOrigin,
        text: htmlScriptElement.text,
        noModule: htmlScriptElement.noModule
    };
}
function createHTMLSelectElementJson(htmlSelectElement) {
	//console.log(htmlSelectElement.selectedIndex);
    return {
        autofocus: htmlSelectElement.autofocus,
        disabled: htmlSelectElement.disabled,
        //form: htmlSelectElement.form, 
        //formSelector: getSelectorFromElement(htmlSelectElement.form).join(" > "), 
        //labels: htmlSelectElement.labels, 
        length: htmlSelectElement.length,
        multiple: htmlSelectElement.multiple,
        name: htmlSelectElement.name,
        //options: htmlSelectElement.options,  
        required: htmlSelectElement.required,
        selectedIndex: htmlSelectElement.selectedIndex,
        //selectedOptions: htmlSelectElement.selectedOptions,  
        size: htmlSelectElement.size,
        type: htmlSelectElement.type,
        validationMessage: htmlSelectElement.validationMessage,
        //validity: htmlSelectElement.validity, 
        value: htmlSelectElement.value,
        willValidate: htmlSelectElement.willValidate
    };
}
function createHTMLSlotElementJson(htmlslotelement) {
    return {
        name: htmlslotelement.name
    }
}
function createHTMLSourceElementJson(htmlSourceElement) {
    return {
        keySystem: htmlSourceElement.keySystem, //experimental
        media: htmlSourceElement.media,
        sizes: htmlSourceElement.sizes,
        src: htmlSourceElement.src,
        srcset: htmlSourceElement.srcset,
        type: htmlSourceElement.type
    };
}
function createHTMLStyleElementJson(htmlStyleElement) {
    return {
        media: htmlStyleElement.media,
        type: htmlStyleElement.type,
        disabled: htmlStyleElement.disabled,
        //sheet: htmlStyleElement.sheet 
    };
}
function createHTMLTableCellElementJson(htmlTableCellElement) {
    return {
        abbr: htmlTableCellElement.abbr,
        cellIndex: htmlTableCellElement.cellIndex,
        colSpan: htmlTableCellElement.colSpan,
        //headers: htmlTableCellElement.headers,   
        rowSpan: htmlTableCellElement.rowSpan,
        scope: htmlTableCellElement.scope
    };
}
function createHTMLTableColElementJson(htmlTableColElement) {
    return {
        span: htmlTableColElement.span
    };
}
function createHTMLTableElementJson(htmlTableElement) {
    return {
        //caption: htmlTableElement.caption,   
        //tHead: htmlTableElement.tHead,    
        //tFoot: htmlTableElement.tFoot,  
        //rows: htmlTableElement.rows, 
        //tBodies: htmlTableElement.tBodies,    
    };
}
function createHTMLTableRowElementJson(htmlTableRowElement) {
    return {
        //cells: htmlTableRowElement.cells, 
        rowIndex: htmlTableRowElement.rowIndex,
        sectionRowIndex: htmlTableRowElement.sectionRowIndex
    };
}
function createHTMLTableSectionElementJson(htmlTableSectionElement) {
    return {
        //rows: htmlTableSectionElement.rows    
    };
}
function createHTMLTemplateElementJson(htmlTemplateElement) {
    return {
        //content: htmlTemplateElement.content 
    };
}
function createHTMLTextAreaElementJson(htmlTextAreaElement) {
    return {
        //form: htmlTextAreaElement.form,   
        //formSelector: getSelectorFromElement(htmlTextAreaElement.form),   
        type: htmlTextAreaElement.type,
        value: htmlTextAreaElement.value,
        textLength: htmlTextAreaElement.textLength,
        defaultValue: htmlTextAreaElement.defaultValue,
        placeholder: htmlTextAreaElement.placeholder,
        rows: htmlTextAreaElement.rows,
        cols: htmlTextAreaElement.cols,
        autofocus: htmlTextAreaElement.autofocus,
        name: htmlTextAreaElement.name,
        disabled: htmlTextAreaElement.disabled,
        //labels: htmlTextAreaElement.labels,   
        maxLength: htmlTextAreaElement.maxLength,
        readOnly: htmlTextAreaElement.readOnly,
        required: htmlTextAreaElement.required,
        selectionStart: htmlTextAreaElement.selectionStart,
        selectionEnd: htmlTextAreaElement.selectionEnd,
        selectionDirection: htmlTextAreaElement.selectionDirection,
        //validity: htmlTextAreaElement.validity,   
        willValidate: htmlTextAreaElement.willValidate,
        validationMessage: htmlTextAreaElement.validationMessage,
        autocomplete: htmlTextAreaElement.autocomplete,
        autocapitalize: htmlTextAreaElement.autocapitalize, //experimental
        inputMode: htmlTextAreaElement.inputMode, //experimental
        wrap: htmlTextAreaElement.wrap
    };
}
function createHTMLTimeElementJson(htmlTimeElement) {
    return {
        dateTime: htmlTimeElement.dateTime
    };
}
function createHTMLTitleElementJson(htmlTitleElement) {
    return {
        text: htmlTitleElement.text
    };
}
function createHTMLTrackElementJson(htmlTrackElement) {
    return {
        kind: htmlTrackElement.kind,
        src: htmlTrackElement.src,
        srclang: htmlTrackElement.srclang,
        label: htmlTrackElement.label,
        m_default: htmlTrackElement.default,    
        readyState: htmlTrackElement.readyState,
        //track: htmlTrackElement.track 
    };
}
function createHTMLVideoElementJson(htmlVideoElement) {
    return {
        //height: htmlVideoElement.height,  
        poster: htmlVideoElement.poster,
        videoHeight: htmlVideoElement.videoHeight,
        videoWidth: htmlVideoElement.videoWidth,
        width: htmlVideoElement.width
    };
}

function getSelectorFromElement(element) {
    if (!(element instanceof Element)) {
        return [];
    }

    const names = [];
    // Generate a CSS selector by traversing up to the root element
    while (element && element.nodeType === Node.ELEMENT_NODE && element.nodeName) {
        let name = element.nodeName.toLowerCase();  

        if (element.id) {
            name += "#" + element.id;
        } else {
            let sib = element;
            let nth = 0;
            while (sib && sib.nodeType === Node.ELEMENT_NODE) {
                nth++;
                sib = sib.previousSibling;
            }
            name += ":nth-child(" + nth + ")"; 
        }
        names.unshift(name);    
        element = element.parentNode;   
    }
    return names;   
}

function customStringify(json) {
    let cache = []; 
    const jsonString = JSON.stringify(json, (key, value) => {
        if (value && cache && typeof value === "object") {
            if (cache.indexOf(value) !== -1) {  
                return; 
            }
            cache.push(value);  
        }
        return value;  
    });
    cache = null; 
    return jsonString;  
}
