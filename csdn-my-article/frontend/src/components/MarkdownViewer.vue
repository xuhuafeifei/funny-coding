<template>
  <div class="markdown-container">
    <!-- 提示信息 -->
    <div class="message-box">
      <p>
        🚨 <strong>温馨提示：</strong>CSDN 启用了防盗链机制，因此图片可能无法在当前页面正常显示，这是正常现象，请不用担心！
      </p>
      <p>
        💡 <strong>解决方案：</strong>你可以点击下方的 <strong>下载按钮</strong>，将文章保存到本地后打开查看，所有内容将会完整呈现！
      </p>
    </div>

    <!-- 文章 ID 和 CSDN Token 输入框和查询按钮 -->
    <div class="token-section">
      <input
        v-model="articleId"
        type="text"
        placeholder="请输入文章 ID"
        class="token-input"
      />
      <!-- <input
        v-model="csdnToken"
        type="text"
        placeholder="请输入 CSDN Token"
        class="token-input"
      /> -->
      <button
        @click="handleQuery"
        :disabled="!articleId"
        class="query-btn"
      >
        查询
      </button>
    </div>

    <!-- 下载选项 -->
    <div class="download-options">
      <label class="radio-option">
        <input
          type="radio"
          name="downloadType"
          value="0"
          v-model="downloadType"
        />
        默认下载（保留 CSDN 默认内容）
      </label>
      <label class="radio-option">
        <input
          type="radio"
          name="downloadType"
          value="1"
          v-model="downloadType"
        />
        图片链接下载（下载存储在 CSDN 的图片资源）
      </label>
      <label class="radio-option">
        <input
          type="radio"
          name="downloadType"
          value="2"
          v-model="downloadType"
        />
        去除水印下载（自动裁剪图片）
      </label>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <button @click="handleDownload" class="download-btn">下载文章</button>
    </div>

    <!-- Markdown 编辑器 -->
    <div ref="editor" id="vditor"></div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import Vditor from 'vditor'
import { get, post, put, del } from '../utils/api';
import 'vditor/dist/index.css'

// 定义组件属性
const props = defineProps({
  content: {
    type: String,
    default: ''
  }
})

// 编辑器 DOM 引用和 Vditor 实例
const editor = ref<HTMLElement | null>(null)
let vditorInstance: Vditor | null = null

// 文章 ID、CSDN Token 和下载选项
const articleId = ref<string>('146302376')
const csdnToken = ref<string>('')
const downloadType = ref<string>('0') // 默认选择 "默认下载"

// 初始化编辑器
onMounted(async () => {
  if (!editor.value) {
    console.error('Editor DOM element is not mounted.')
    return
  }

  try {
    vditorInstance = new Vditor('vditor', {
      mode: "sv",
      lang: "zh_CN",
      theme: "light",
      toolbarConfig: { pin: true },
      cache: {
        enable: false
      },
      preview: {
        theme: {
          current: "idea-light",
        },
        maxWidth: "100%", // 设置预览框的最大宽度为 100%
        actions: [], // 关闭额外工具栏
        hljs: { style: "dark" },
        math: {
          inlineDigit: true
        }
      },
      value: "nihao"
    })
  } catch (error) {
    console.error('初始化编辑器失败:', error)
  }
})

// 查询按钮逻辑
const handleQuery = () => {
  if (!articleId.value.trim()) {
    alert('请先填写文章 ID 和 CSDN Token！')
    return
  }

  // 模拟查询逻辑（可以根据实际需求替换为 API 请求）
  console.log('查询中，文章 ID:', articleId.value, 'CSDN Token:', csdnToken.value)
  const data = post("/api/getArticleContent", { "articleId":articleId.value });
  data.then((data) => {
    console.log(data); 
    if (vditorInstance) {
      vditorInstance.setValue(data.data.data); // 假设 data 是查询到的 Markdown 内容
  }
  })
  // alert('查询成功！可以继续操作。')
}

// 下载 Markdown 文件
const handleDownload = () => {
  if (!vditorInstance) return
  console.log(downloadType.value)
  // const data = post("/api/downloadArticle", { "articleId":articleId.value, "downloadType":downloadType });
  // data.then((data) => {
  //   console.log(data);
  // })
      // 构造请求数据
    const postData = {
      articleId: articleId.value,
      downloadType: downloadType.value // 确保使用 .value
    };

    window.location.href = 'http://localhost:8080/api/downloadArticleSimple?articleId=' + postData.articleId + '&downloadType=' + postData.downloadType;
}
</script>

<style scoped>
.markdown-container {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
  font-family: Arial, sans-serif;
}

.message-box {
  padding: 16px;
  border-radius: 8px;
  background-color: #f9f9f9;
  border: 1px solid #e1e4e8;
  color: #333;
  line-height: 1.6;
  max-width: 800px;
  margin: 20px auto;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.token-section {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 20px auto;
  max-width: 800px;
}

.token-input {
  padding: 10px 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
  width: 200px;
  margin-right: 16px;
}

.query-btn {
  padding: 10px 20px;
  background: linear-gradient(135deg, #6e8efb, #a777e3);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  font-size: 14px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.query-btn:hover {
  background: linear-gradient(135deg, #5d7de8, #9666d8);
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.query-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.download-options {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin: 20px auto;
  max-width: 800px;
}

.radio-option {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #333;
}

.radio-option input[type="radio"] {
  margin-right: 8px;
}

.toolbar {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 16px;
}

.download-btn {
  padding: 10px 24px;
  background: linear-gradient(135deg, #6e8efb, #a777e3);
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  font-size: 14px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.download-btn:hover {
  background: linear-gradient(135deg, #5d7de8, #9666d8);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

/* 强制 Vditor 内容左对齐 */
#vditor {
  text-align: left !important;
}

.vditor-reset {
  text-align: left !important;
}

.vditor-toolbar {
  text-align: left !important;
}

.vditor-preview {
  text-align: left !important;
}

@media (max-width: 768px) {
  .token-section {
    flex-direction: column;
    align-items: flex-start;
  }

  .token-input {
    width: 100%;
    margin-right: 0;
    margin-bottom: 10px;
  }

  .query-btn {
    width: 100%;
  }
}
</style>