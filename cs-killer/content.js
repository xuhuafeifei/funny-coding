// 存储原始搜索词
let originalQuery = '';
let excludes = [];

// 从存储加载排除项
chrome.storage.sync.get('excludes', (data) => {
    excludes = data.excludes || ['csdn'];
});

// 生成排除字符串
function getExcludeString() {
    return excludes.map(item => `-${item}`).join(' ');
}

chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
    if (request.type === 'add') {
        excludes.push(request.newData)

        // 修改输入框内的内容
        const searchInput = document.querySelector('input[name="q"]');
        const modifyItem = '-' + request.newData
        if (searchInput && ! searchInput.value.includes(modifyItem)) {
            searchInput.value = searchInput.value + ' ' + modifyItem;
        }

    } else if (request.type === 'remove') {
        const index = excludes.indexOf(request.origin);
        if (index !== -1) {
            excludes.splice(index, 1);
        }
        console.log('remove', request.origin, ' excludes = ', excludes.join(','))

        // 移除输入框内的内容
        const searchInput = document.querySelector('input[name="q"]');
        const removeItem = '-' + request.origin

        if (searchInput && searchInput.value.includes(removeItem)) {
            searchInput.value = searchInput.value.replace(removeItem, '').trim();
        }

    } else if (request.type == 'modify') {
        // 修改输入框内的内容
        const searchInput = document.querySelector('input[name="q"]');
        const modifyItem = '-' + request.origin
        if (searchInput && searchInput.value.includes(modifyItem)) {
            console.log("modifyItem = ", modifyItem, " newData = ", request.newData)
            searchInput.value = searchInput.value.replace(modifyItem, '-' + request.newData).trim();
        }
        
        const index = excludes.indexOf(request.origin);
        if (index !== -1) {
            excludes[index] = request.newData;
        }
        console.log('modify', request.origin, ' excludes = ', excludes.join(',')) 
    }
});


function modifySearchInput() {
    const searchInput = document.querySelector('input[name="q"]');
    if (searchInput && !searchInput.dataset.modified) {
        searchInput.dataset.modified = "true";

        const excludeStr = getExcludeString();

        // 聚焦时移除排除项
        searchInput.addEventListener('focus', function () {
            excludes.forEach(item => {
                item = '-' + item
                if (this.value.includes(item)) {
                    this.value = this.value.replace(item, '').trim();
                }
            })
        });

        searchInput.addEventListener('keydown', function (e) {
            if (e.key === 'Enter') {
                excludes.forEach(item => {
                    item = '-' + item
                    if (this.value.includes(item)) {
                        this.value = this.value.replace(item, '').trim();
                    } else {
                        this.value += ` ${item}`;
                    }
                })
            }
        });

        // 失去焦点时加回排除项
        searchInput.addEventListener('blur', function () {
            excludes.forEach(item => {
                item = '-' + item
                if (!this.value.includes(item)) {
                    this.value += ` ${item}`;
                }
            })
        });

        // 初始处理
        if (searchInput.value && !searchInput.value.includes(excludeStr)) {
            searchInput.value += ` ${excludeStr}`;
        }
    }
}

// 监听存储变化
chrome.storage.onChanged.addListener((changes) => {
    if (changes.excludes) {
        excludes = changes.excludes.newValue || [];
    }
});

// 初始执行
modifySearchInput();

// 监听动态内容加载
new MutationObserver(modifySearchInput).observe(
    document.body,
    { childList: true, subtree: true }
);