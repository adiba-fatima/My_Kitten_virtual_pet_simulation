from PIL import Image
import os

img_path = r"C:/Users/victus/AndroidStudioProjects/MyKitten/app/src/main/res/drawable/yawn.png"
if not os.path.exists(img_path):
    print(f"Error: {img_path} not found.")
else:
    img = Image.open(img_path).convert("RGBA")
    datas = img.getdata()

    new_data = []
    for item in datas:
        # If pixel is close to white (R>230, G>230, B>230), make it transparent
        if item[0] > 230 and item[1] > 230 and item[2] > 230:
            new_data.append((255, 255, 255, 0))
        else:
            new_data.append(item)

    img.putdata(new_data)
    img.save(img_path, "PNG")
    print("Background removed successfully from yawn.png")
