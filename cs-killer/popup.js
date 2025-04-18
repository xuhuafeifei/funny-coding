document.addEventListener('DOMContentLoaded', async () => {
    const excludeList = document.getElementById('excludeList');
    const newExclude = document.getElementById('newExclude');
    const addBtn = document.getElementById('addBtn');

    // 从存储加载排除项
    let { excludes = [] } = await chrome.storage.sync.get('excludes');

    // 渲染排除列表
    function renderExcludes() {
        excludeList.innerHTML = '';
        excludes.forEach((item, index) => {
            const div = document.createElement('div');
            div.className = 'item';

            // item框渲染
            const input = document.createElement('input');
            input.type = 'text';
            input.value = item;
            input.addEventListener('input', (e) => {
                origin = excludes[index];
                excludes[index] = e.target.value.trim();
                saveExcludes();
                setMsg('modify', origin, excludes[index]);
            });

            // 删除按钮
            const btn = document.createElement('button');
            btn.textContent = '×';
            btn.addEventListener('click', () => {
                excludes.splice(index, 1);
                saveExcludes();
                setMsg('remove', item, null);
                renderExcludes();
            });

            // 添加div item
            div.appendChild(input);
            div.appendChild(btn);
            excludeList.appendChild(div);
        });
    }

    /**
     * 发送消息通知content.js
     * @param {*} typeName : remove/ add/ modify
     * @param {*} msg : item content
     */
    function setMsg(typeName, originData, newData) {
        // 发送给content.js
        chrome.tabs.query({ active: true, currentWindow: true }, (tabs) => {
            chrome.tabs.sendMessage(tabs[0].id, {
                type: typeName,
                origin: originData,
                newData: newData
            });
        });
    }

    // 保存排除项
    function saveExcludes() {
        chrome.storage.sync.set({ excludes });
    }

    // 添加新排除项
    addBtn.addEventListener('click', () => {
        const value = newExclude.value.trim();
        if (value && excludes.includes(value)) {
            alert('该排除项已存在');
            return;
        }
        if (value) {
            excludes.push(value);
            saveExcludes();
            setMsg('add', null, value);
            renderExcludes();
            newExclude.value = '';
        }
    });

    // 初始渲染
    renderExcludes();
});