package com.facevel.inc.app.views
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.facevel.inc.app.R
import com.facevel.inc.app.databinding.CardComponentNotificationViewBinding
import com.facevel.inc.app.utils.makeGone
import com.facevel.inc.app.utils.makeVisible

class CardComponentNotificationView(context: Context, attrs: AttributeSet) : LinearLayout(
    context, attrs
) {
    lateinit var binding: CardComponentNotificationViewBinding

    private fun init(context: Context, attrs: AttributeSet) {
        val view = inflate(context, R.layout.card_component_notification_view, this)
        binding = CardComponentNotificationViewBinding.bind(view)


        val attributes =
            context.obtainStyledAttributes(attrs, R.styleable.CardComponentNotificationView)

        val cardType: String =
            attributes.getString(R.styleable.CardComponentNotificationView_card_type) ?: "info"

        var textImageColor = ContextCompat.getColor(context, R.color.orange_light_dark_text)
        // var backgroundColor = ContextCompat.getColor(context, R.color.orange_light_dark_bg)
        println("Cardtype $cardType")
        when (cardType) {
            "info" -> {
                textImageColor = ContextCompat.getColor(context, R.color.blue_light_dark_text)
                binding.cardParentLayout.setBackgroundResource(R.drawable.rounded_borders_solid_stroke_info)
            }
            "success" -> {
                textImageColor = ContextCompat.getColor(context, R.color.green_light_dark_text)
                binding.cardParentLayout.setBackgroundResource(R.drawable.rounded_borders_solid_stroke_success)
                val value = binding.cardParentLayout.background

            }
            "error" -> {
                textImageColor = ContextCompat.getColor(context, R.color.red_light_dark_text)
                binding.cardParentLayout.setBackgroundResource(R.drawable.rounded_borders_solid_stroke_error)
            }
        }
        val isDescriptionShort: Boolean = attributes.getBoolean(
            R.styleable.CardComponentNotificationView_card_short_description, false
        )
        val isActionLayoutHidden: Boolean =
            attributes.getBoolean(R.styleable.CardComponentNotificationView_card_hide_action, true)
        val titleText: String =
            attributes.getString(R.styleable.CardComponentNotificationView_card_title_text) ?: ""
        val descriptionText: String =
            attributes.getString(R.styleable.CardComponentNotificationView_card_description_text)
                ?: ""

        val headingText =
            attributes.getString(R.styleable.CardComponentNotificationView_card_heading_text)
                ?: ""

        val actionText =
            attributes.getString(R.styleable.CardComponentNotificationView_card_action_text) ?: ""

        val actionIcon: Int = attributes.getInt(
            R.styleable.CardComponentNotificationView_card_action_icon, R.drawable.ic_uninstall
        )
        val baseIcon: Int = attributes.getInt(
            R.styleable.CardComponentNotificationView_card_base_icon, R.drawable.ic_uninstall
        )


        if (isActionLayoutHidden) {
            binding.notificationCardActionLayout.makeGone()
        } else {
            binding.notificationCardActionLayout.makeVisible()
        }


        binding.apply {
            this.notificationCardHeading.text = headingText
            this.notificationCardDescription.text = descriptionText
            this.notificationCardActionText.text = actionText
            this.notificationCardTitle.text = titleText



            this.notificationCardBaseIcon.setImageResource(baseIcon)
            this.notificationCardActionIcon.setImageResource(actionIcon)
            this.notificationCardTitle.setTextColor(textImageColor)
            this.notificationCardHeading.setTextColor(textImageColor)
            this.notificationCardDescription.setTextColor(textImageColor)
            if (isDescriptionShort) {
                binding.notificationCardDescription.apply {
                    maxLines = 1
                    ellipsize
                }
            }
            this.notificationCardActionText.setTextColor(textImageColor)
            this.notificationCardBaseIcon.setColorFilter(textImageColor)
            this.notificationCardActionIcon.setColorFilter(textImageColor)
        }


    }


    init {
        init(context, attrs)
    }


}