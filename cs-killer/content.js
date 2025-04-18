// 存储原始搜索词
let originalQuery = '';

function modifySearchInput() {
    const searchInput = document.querySelector('input[name="q"]');
    if (searchInput && !searchInput.dataset.modified) {
        searchInput.dataset.modified = "true";

        // 聚焦时移除 -csdn
        searchInput.addEventListener('focus', function () {
            if (this.value.endsWith(' -csdn')) {
                originalQuery = this.value.replace(' -csdn', '');
                this.value = originalQuery;
            }
        });

        // 失去焦点时加回 -csdn (如果用户没有修改)
        searchInput.addEventListener('blur', function () {
            if (originalQuery &&
                this.value === originalQuery &&
                !this.value.includes('-csdn')) {
                this.value += ' -csdn';
            }
        });

        // 处理表单提交
        const form = searchInput.closest('form');
        if (form) {
            form.addEventListener('submit', function (e) {
                if (!searchInput.value.includes('-csdn')) {
                    searchInput.value += ' -csdn';
                }
            });
        }

        // 初始处理
        if (searchInput.value && !searchInput.value.includes('-csdn')) {
            searchInput.value += ' -csdn';
        }
    }
}

// 初始执行
modifySearchInput();

// 监听动态内容加载
new MutationObserver(modifySearchInput).observe(
    document.body,
    { childList: true, subtree: true }
);
