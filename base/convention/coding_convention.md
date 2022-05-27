# Android Coding Convention

## Trong class

### I. Quy tắc đặt tên biến

#### 1. Biến toàn cục val:
Viết hoa toàn bộ, từ cách nhau bởi `_`.

```Kotlin
val NAME_PROPERTY = "immutable"
```

#### 2. Biến toàn cục var:
Viết thường, camel style

```Kotlin
var nameProperty: String
```

### II. Quy tắc đặt tên hàm

Viết thường, camel style

```Kotlin
fun onInitView()

fun isSelected(): Boolean
```

### III. Quy tắc sắp xếp inner class, inner interface

Inner interface đặt ở trên cùng của outer class

Commanion object đặt trên cùng của outer class, dưới inner interface

Inner class đặt ở cuối của outer class, ngăn cách với func của outer class bởi 2 dòng

```Kotlin
class ExampleGroup : GroupData<ExampleData?> {

    interface Listener {
        fun onClickItem()
    }

    companion object {
        private const val EXAMPLE_TYPE = "EXAMPLE_TYPE"
    }

    /** code của outer class **/

    inner class ExampleVH(viewDataBinding: ViewDataBinding) :
            GroupVH<ExampleData?, ExampleGroup>(viewDataBinding) {
        /** code của inner class **/
    }
}
```

### IV. Quy tắc sắp xếp biến trong class

Các variable trong class được sắp xếp theo thứ tự:

- `static` variable
- `public` variable
- `protected` variable
- Các variable về `view`
- `private` variable

### V. Quy tắc sắp xếp func trong class

Các func trong class được sắp xếp theo thứ tự:

- func overrided từ Android SDK
- func overrided từ Base Framework
- func overrided từ các interface/abstract class của thirdparty lib
- func overrided từ các interface/abstract class tự định nghĩa
- `abstract` func
- `public` func
- `private` func

## Trong resource

### I. Quy tắc đặt tên layout

#### 1. Layout của Activity:
Viết thường, từ cách nhau bởi `_`, kết thúc bằng hậu tố `activity`.

```
main_activity.xml
```

#### 2. Layout của Fragment:
Viết thường, từ cách nhau bởi `_`, kết thúc bằng hậu tố `fragment`.

```
home_fragment.xml
```

#### 3. Layout của item trong RecyclerView/ListView:
Viết thường, từ cách nhau bởi `_`, kết thúc bằng hậu tố `item`.

```
search_result_item.xml
```

#### 4. Layout của 1 custom view:
Viết thường, từ cách nhau bởi `_`, kết thúc bằng hậu tố `layout`.

```
my_profile_layout.xml
```

### II. Quy tắc đặt tên id trong layout

#### 1. Tên id trong activity.xml:
tên_kiểu_view + tên_activity + tên view.

Ví dụ: trong `main_activity.xml` có `TextView` user name -> `TextView` có id là:

```
    @+id/tvMainUserName
```

#### 2. Tên id trong fragment.xml:
tên_kiểu_view + tên_fragment + tên view.

Ví dụ: trong `home_fragment.xml` có `TextView` user name -> `TextView` có id là

```
    @+id/tvHomeUserName
```

#### 3. Tên id trong item.xml:
tên_kiểu_view + tên_item + tên view.

Ví dụ: trong `search_result_item.xml` có `TextView` user name -> `TextView` có id là

```
    @+id/tvSearchResultHotelName
```

#### 4. Tên id trong layout.xml:
tên_kiểu_view + tên_layout + tên view.

Ví dụ: trong `my_profile_layout.xml` có `TextView` user name -> `TextView` có id là

```
    @+id/tvMyProfileUserName
```

### III. Quy tắc đặt tên drawable

#### 1. Drawable có 1 trạng thái:
shape_(hình dạng)_color_bg_color_stroke_corner.xml.

Drawable hình chữ nhật có 1 trạng thái, background là primary_color (đc định nghĩa trong file color.xml), stroke màu
grey, và bo 4 góc với radius large (định nghĩa corner_large trong dimen.xml)

```
shape_pri_bg_grey_stroke_corner_large.xml
```

Drawable hình tròn có 1 trạng thái, background là primary_color (đc định nghĩa trong file color.xml), stroke màu grey,
và bo 4 góc với radius large (định nghĩa corner_large trong dimen.xml)

```
shape_cir_pri_bg_grey_stroke_corner_large.xml
```

##### 2. Drawable có nhiều trạng thái:
selector_(hình dạng)_color_bg_color_selected_bg_color_stroke_corner.xml.

Drawable hình chữ nhật có nhiều status (pressed, normal, selected,…), có background màu đỏ, stroke màu grey, và không bo
góc. Màu background khi select đậm hơn (trường hợp default)

```
selector_red_color_grey_stroke.xml
```

Drawable hình chữ nhật có nhiều status (pressed, normal, selected,…), có background màu đỏ, stroke màu grey, và không bo
góc. Màu background khi select là màu xanh

```
selector_red_color_blue_selected_grey_stroke.xml
```

Drawable hình tròn có nhiều status (pressed, normal, selected,…), có background màu đỏ, stroke màu grey. Màu background
khi select đậm hơn (trường hợp default)

```
selector_cir_red_color_grey_stroke.xml
```

Drawable hình tròn có nhiều status (pressed, normal, selected,…), có background màu đỏ, stroke màu grey. Màu background
khi select là màu xanh

```
selector_cir_red_color_blue_selected_grey_stroke.xmlml
```

#### 3. Drawable là ảnh:
ic_tên ảnh.

```
ic_launch_24px.png
```

### IV. Quy tắc sắp xếp attrs trong xml

Các attrs trong xml được sắp xếp theo thứ tự:

- id
- Kích thước: width, height
- Vị trí. VD: align_parent_top,...
- Margin
- Padding
- Các thuộc tính nội tại của View
- Các attrs của namespace khác. VD app:constraint_toStart....

## Bảng viết tắt các kiểu view

|        Tên View        |   Viết tắt  | Tên id view trong lauout [(*)](#chú-thích) | Tên view trong class kt, java [(**)](#chú-thích)|
|------------------------|-------------|--------------------------------------------|-------------------------------------------------|
| TextView               |  tv         | tvLoginUserName                            | tvUserName                                      |
| EditText               |  edt        | edtLoginUserName                           | edtUserName                                     |
| Button                 |  btn        | btnLogin                                   | btnLogin                                        |
| View                   |  v          | vLoginDivider                              | vDivider                                        |
| ImageView              |  iv         | ivLoginShowPass                            | ivShowPass                                      |
| LinearLayout           |  ll         | llLoginRoot                                | llRoot                                          |
| RelativeLayout         |  rl         | rlLoginRoot                                | rlRoot                                          |
| FrameLayout            |  fl         | flLoginRoot                                | flRoot                                          |
| ViewPager              |  vp         | vpHomeNews                                 | vpNews                                          |
| RecyclerView           |  rv         | rvHomeNewsData                             | rvNewsData                                      |
| Checkbox               |  cb         | cbLoginRemember                            | cbRemember                                      |
| ConstraintLayout       |  const      | constLoginRoot                             | constRoot                                       |
| CoordinateLayout       |  coor       | coorLoginRoot                              | coorRoot                                        |
| NestedScrollView       |  nscrl      | nscrlLogin                                 | nscrlLogin                                      |
| CardView               |  cv         | cvUserInfoAvatar                           | cvAvatar                                        |
| RadioButton            |  rdBtn      | rdBtnRegisterGender                        | rdBtnGender                                     |
| RadioGroup             |  rdGrp      | rdGrpRegisterGender                        | rdGrpGender                                     |
| ScrollView             |  srcv       | srcvLogin                                  | srcvLogin                                       |
| TabLayout              |  tab        | tabHomeNews                                | tabNews                                         |
| GridLayout             |  grl        | grlLoginRoot                               | grlRoot                                         |
| ToggleButton           |  tglBtn     | tglBtnVideoMute                            | tglBtnMute                                      |
| TextInputLayout        |  til        | tilLoginUserName                           | tilUserName                                     |
| TextInputEditText      |  edt        | edtLoginUserName                           | edtUserName                                     |
| ActionBarView          |  abv        | abvHomeNews                                | abvNews                                         |
| ProgressBar            |  prb        | prbRegisterProcess                         | prbProcess                                      |
| Switch                 |  sw         | swSettingNotify                            | swNotify                                        |
| DoubleActionButtonView |  dabv       | dabvLogin                                  | dabvLogin                                       |
| AppBarLayout           |  apbl       | apblHomeNews                               | apblNews                                        |

#### Chú thích:

- [(*)](#bảng-viết-tắt-các-kiểu-view): Bắt buộc đặt theo [quy tắc đặt tên id trong layout](#ii-quy-tắc-đặt-tên-id-trong-layout)
- [(**)](#bảng-viết-tắt-các-kiểu-view): Không bắt buộc phải đặt theo tên class, chỉ cần có tiền tố của view. Ví dụ: Trong `LoginActivity` có `TextView` user name  -> chỉ cần đặt là: `tvUserName` là được.
