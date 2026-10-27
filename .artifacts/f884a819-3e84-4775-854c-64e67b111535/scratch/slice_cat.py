from PIL import Image
import os

img_path = r"C:/Users/victus/AndroidStudioProjects/MyKitten/app/src/main/res/drawable/image_28795b23__1_.jpg"
output_dir = r"C:/Users/victus/AndroidStudioProjects/MyKitten/app/src/main/res/drawable"

img = Image.open(img_path).convert("RGBA")
width, height = img.size

print(f"Image size: {width}x{height}")

# The image is a 3x3 grid of sub-images
cols = 3
rows = 3
cell_w = width // cols
cell_h = height // rows

# Unique frames correspond to grid positions:
# Row 0, Col 0: Frame 1
# Row 0, Col 1: Frame 2
# Row 0, Col 2: Frame 3
# Row 1, Col 2: Frame 4
# Row 2, Col 0: Frame 5 (wait, let's inspect the positions of Frame 1 to 5)

# Let's crop each cell, remove white background, and save unique frames:
# Frame 1: cell (0, 0)
# Frame 2: cell (0, 1)
# Frame 3: cell (0, 2)
# Frame 4: cell (1, 2)
# Frame 5: cell (2, 0)

frame_coords = [
    (0, 0, "cat_idle_1.png"),
    (0, 1, "cat_idle_2.png"),
    (0, 2, "cat_idle_3.png"),
    (1, 2, "cat_idle_4.png"),
    (2, 0, "cat_idle_5.png")
]

for r, c, filename in frame_coords:
    box = (c * cell_w, r * cell_h, (c + 1) * cell_w, (r + 1) * cell_h)
    cell = img.crop(box)

    # Crop slightly from bottom to remove text label ("Frame X")
    cw, ch = cell.size
    cell = cell.crop((0, 0, cw, int(ch * 0.85)))

    # Remove white background
    datas = cell.getdata()
    new_data = []
    for item in datas:
        # If pixel is close to white (e.g. R>240, G>240, B>240), make it transparent
        if item[0] > 240 and item[1] > 240 and item[2] > 240:
            new_data.append((255, 255, 255, 0))
        else:
            new_data.append(item)
    cell.putdata(new_data)

    out_path = os.path.join(output_dir, filename)
    cell.save(out_path, "PNG")
    print(f"Saved {out_path}")
