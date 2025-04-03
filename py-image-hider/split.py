'''
分割函数, 服务于我制作PPT
'''

import cv2
from PIL import Image
import numpy as np

# 需要切分几行
N = 3

file_path = './input/shenli.jpg'

# 读取图片
img = cv2.imread(file_path)

# 图片大小
h, w, c = img.shape
# 图片宽度
w_per_row = w // N
# 计算每行的高度
row_height = h // N

def split_img(img, row_height):
    # 切分图片
    img_list = []
    for i in range(N * N):
        # 计算当前行的起始和结束位置
        start_h = i // N * row_height
        end_h = start_h + row_height
        start_w = (i % N) * w_per_row
        end_w = start_w + w_per_row
        # 切割图片
        img_list.append(img[start_h:end_h, start_w:end_w])
    return img_list

# 切分图片
img_list = split_img(img, row_height)

# 保存切分后的图片
for i, img in enumerate(img_list):
    # cv2.imwrite(f'./output/bulaien/split_{i}.jpg', img)

    # 保存切分后的图片为PNG格式
    img_pil = Image.fromarray(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))
    img_pil.save(f'./output/shenli/split_{i}.png')

print('切分完成')