from PIL import Image
import numpy as np
from main4 import N
from main4 import large_scale

merge_file_path = "image4/result-291600x540.png"
info = merge_file_path.split("-")[-1].split(".png")[0].split("x")
cnt, t_col = int(info[0]), int(info[1])

print(cnt)

# 读取图像数据
img = np.array(Image.open(merge_file_path))
shape = img.shape
col = shape[1]

# 初始化最终图像
res_image = []
row = []

step = N // 2

for i in range(step, shape[0], step * 2 * large_scale):
    if cnt == 0:
        break
    for j in range(step, shape[1], step * 2 * large_scale):
        if cnt == 0:
            break
        if len(row) == t_col:
            res_image.append(row)
            row = []
        row.append(img[i][j])
        cnt -= 1

if len(row) != 0:
    if len(row) != t_col:
        # 如果row不足t_col, 后置补充0
        row += [0] * (t_col - len(row))
        res_image.append(row)
        row = []
    else:
        res_image.append(row)

# 转换为 uint8 并保存
res_image = np.array(res_image)
Image.fromarray(res_image).save("./image4/decode.png")
