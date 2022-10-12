package com.facevel.inc.app.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.facevel.inc.app.R
import com.facevel.inc.app.databinding.BaseCardComponentViewElementBinding
import com.facevel.inc.app.utils.makeGone
import com.facevel.inc.app.utils.makeVisible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class CardComponentViewOutline(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs) {

    lateinit var binding: BaseCardComponentViewElementBinding

    private fun init(context: Context, attrs: AttributeSet) {

        val view = inflate(
            context,
            R.layout.base_card_component_view_element_outline,
            this
        )
        binding = BaseCardComponentViewElementBinding.bind(view)


        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CardComponentViewOutline)
        val iconColorResource: Int = attributes.getColor(
            R.styleable.CardComponentView_card_icon_color,
            ContextCompat.getColor(context, R.color.colorAccent)
        )
        val iconResource: Drawable? = attributes.getDrawable(
            R.styleable.CardComponentView_card_icon
        )

        val isTagHidden: Boolean =
            attributes.getBoolean(R.styleable.CardComponentViewOutline_card_hide_tag, true)

        val isCardInactive: Boolean =
            attributes.getBoolean(R.styleable.CardComponentViewOutline_card_inactive, false)

       // if (isCardInactive) makeCardInactive()

        if (isTagHidden) makeTagVisible(!isTagHidden)

        val titleText: String =
            attributes.getString(R.styleable.CardComponentViewOutline_card_title_text) ?: ""
        val subTitleText: String =
            attributes.getString(R.styleable.CardComponentViewOutline_card_subtitle_text) ?: ""

        val descriptionText: String =
            attributes.getString(R.styleable.CardComponentViewOutline_card_description_text) ?: ""

        val tagText: String? =
            attributes.getString(
                R.styleable.CardComponentViewOutline_card_tag_text
            )


        setIcon(iconResource)
        setIconColor(iconColorResource)
        setTitleText(titleText)
        setDescriptionText(descriptionText)
        setCardSubTitle(subTitleText)
        if (tagText.isNullOrEmpty().not()) {
            makeTagVisible(true)
            setTagText(tagText!!)
        }

    }


    fun setTitleText(title: String) {
        binding.cardTitle.text = title
    }

    fun setIconColor(colorResource: Int) {
        binding.cardIcon.setColorFilter(colorResource)
    }

    fun setDescriptionText(description: String) {
        binding.cardDescriptionText.text = description
    }

    fun setIconResource(iconResource: Int) {
        binding.cardIcon.setImageResource(iconResource)
    }

    fun setIcon(iconResource: Drawable?) {
        if (iconResource != null)
            binding.cardIcon.setImageDrawable(iconResource)
    }

    fun setTagText(tag: String) {
        binding.cardTagText.text = tag
    }

    fun makeTagVisible(boolean: Boolean) {
        if (boolean) {
            binding.cardTagCard.makeVisible()
        } else {
            binding.cardTagCard.makeGone()
        }
    }

    fun setCardSubTitle(subTitle: String) {
        binding.cardSubTitle.text = subTitle
    }

    fun makeProgressDeterminate() {
        binding.cardProgressBar.makeGone()
        binding.cardProgressBar.isIndeterminate = false
        binding.cardProgressBar.makeVisible()
    }

    fun showProgress() {
        binding.cardProgressBar.makeVisible()
        binding.cardIcon.makeGone()
    }

    fun hideProgress() {
        binding.cardProgressBar.makeGone()
        binding.cardIcon.makeVisible()
    }

    fun markAsComplete() {
      //  makeCardInactive()
        binding.cardCompletedCheck.makeVisible()
    }

    fun setProgress(progress: Int) {
        binding.cardProgressBar.setProgress(progress, true)
    }

    fun limitDescriptionLines(limit: Int) {
        binding.cardDescriptionText.maxLines = limit
    }


//    fun makeCardInactive() {
//        binding.apply {
//            this.cardProgressBar.makeGone()
//            this.cardIcon.makeVisible()
//            this.cardTitle.animateTextColorChange(R.color.card_view_text_color_inactive)
//            this.cardDescriptionText.animateTextColorChange(R.color.card_view_text_color_inactive)
//            this.cardSubTitle.animateTextColorChange(R.color.card_view_text_color_inactive)
//            this.cardIcon.animateIconFilterChange(R.color.card_view_text_color_inactive)
//        }
//    }
//
//    fun makeCardActive(iconColorResource: Int) {
//        binding.apply {
//            this.cardTitle.animateTextColorChange(R.color.primaryTextColorDark)
//            this.cardDescriptionText.animateTextColorChange(R.color.primaryTextColorLight)
//            this.cardSubTitle.animateTextColorChange(R.color.primaryTextColorLight)
//            this.cardIcon.animateIconFilterChange(iconColorResource)
//        }
//    }iconColorResource


    init {
        init(context, attrs)
    }


}
