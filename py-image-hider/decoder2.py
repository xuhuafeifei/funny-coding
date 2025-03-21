from PIL import Image
import numpy as np

merge_file_path = "image2/result-291600.png"
cnt = int(merge_file_path.split("-")[-1].split(".png")[0])

print(cnt)

# 读取图像数据
img = np.array(Image.open(merge_file_path))
shape = img.shape
col = shape[1]
t_col = col // 2

# 初始化最终图像
res_image = []

for i in range(1, shape[0], 2):
    row = []
    if cnt == 0:
        break
    for j in range(1, shape[1], 2):
        if cnt == 0:
            break
        row.append(img[i][j])
        cnt -= 1
    # 如果row不足t_col, 后置补充0
    row += [0] * (t_col - len(row))

    # 这里正确地按行拼接
    res_image.append(row)

# 转换为 uint8 并保存
res_image = np.array(res_image)
Image.fromarray(res_image).save("./image2/decode.png")
