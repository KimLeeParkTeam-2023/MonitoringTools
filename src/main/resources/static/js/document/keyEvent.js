/* 본문 작성할 div 생성 */
function createDiv(eventDiv) {
    var contentWrap = document.createElement("div");
    contentWrap.classList = "document-content-wrap";

    var content = document.createElement("div");
    content.setAttribute("contenteditable", "true");
    content.classList.add("document-content-div");

    content.addEventListener("focusin", function (event) {
        focusing(event.target);
    });

    content.addEventListener("blur", function (event) {
        unfocusing(event.target);
    });

    if(eventDiv.parentNode.nodeName === 'LI'){
        var newItem = document.createElement('li');
        newItem.appendChild(content);
        contentWrap.appendChild(newItem);
    }else{
        contentWrap.appendChild(content);
    }
    return contentWrap;
}

/* 본문 div 삭제 */
function deleteDiv(eventDiv) {
    contentWrap = eventDiv.parentNode;

    if (eventDiv.innerHTML === "") {
        const preDiv = contentWrap.previousElementSibling;
        const nextDiv = contentWrap.nextElementSibling;
        contentWrap.remove();
        if (preDiv) {
            preDiv.querySelector('.document-content-div').focus();
        } else if (nextDiv) {
            nextDiv.querySelector('.document-content-div').focus();
        } else{
            document.getElementById("content").innerHTML = `<div class="document-content-explanation">
                                                                    <h2>단축키 설명</h2>
                                                                    <div style="text-align: left;">
                                                                    # => h1<br>
                                                                    ## => h2<br>
                                                                    ### => h3<br>
                                                                    - => •<br>
                                                                    </div>
                                                                </div>`;
        }
    }
}

/* 윗쪽 화살표 : 위로 커서 이동 */
function cursorFocusUp(eventDiv) {
    const newFocusDiv = eventDiv.parentNode.previousElementSibling;
    if (newFocusDiv) {
        newFocusDiv.querySelector('.document-content-div').focus();
    }
}

/* 아래쪽 화살표 : 아래로 커서 이동 */
function cursorFocusDown(eventDiv) {
    const newFocusDiv = eventDiv.parentNode.nextElementSibling;
    if (newFocusDiv) {
        newFocusDiv.querySelector('.document-content-div').focus();
    }
}

/* focusing 되었을 때 */
function focusing(eventDiv) {
    eventDiv.setAttribute("placeholder", "여기에 텍스트를 입력하세요");
    moveCursorEnd(eventDiv);
}

/* focusing에서 벗어났을 때 */
function unfocusing(eventDiv) {
    eventDiv.removeAttribute("placeholder");
}

/* 입력 창에 입력 커서를 맨 뒤로 이동 */
function moveCursorEnd(eventDiv) {
    setTimeout(() => {
        const range = document.createRange();
        const selection = window.getSelection();
        range.selectNodeContents(eventDiv);
        range.collapse(false);
        selection.removeAllRanges();
        selection.addRange(range);
    }, 0);
}

/* Tab을 선택 시 */
function insertTab(eventDiv) {
    contentWrap = eventDiv.parentNode;
    var paddingValue = parseFloat(contentWrap.style.paddingLeft);
    
    if (isNaN(paddingValue)) {
        paddingValue = 0;
    }
    
    paddingValue += 1.5;
    contentWrap.style.paddingLeft = paddingValue + 'em';
}

/* Shift + Tab 선택 시 */
function deleteTab(eventDiv) {
    contentWrap = eventDiv.parentNode;
    var paddingValue = parseFloat(contentWrap.style.paddingLeft);
    
    if (isNaN(paddingValue)) {
        paddingValue = 0;
    }
    
    paddingValue -= 1.5;
    contentWrap.style.paddingLeft = paddingValue + 'em';
}