package com.github.renshaodong.rplugin.entity

data class IconFont(
    val css_prefix_text: String, // icon-
    val description: String,
    val font_family: String, // iconfont
    val glyphs: List<Glyph>,
    val id: String,
    val name: String
)

data class Glyph(
    val font_class: String, // fengzheng
    val icon_id: String, // 20519874
    val name: String, // 风筝
    val unicode: String, // e614
    val unicode_decimal: Int // 58900
)
