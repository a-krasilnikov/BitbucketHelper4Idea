package ui

import bitbucket.data.PRParticipant
import com.intellij.util.ui.UIUtil
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import javax.swing.JLabel

object ReviewerComponentFactory {
    private const val imageSize = 24
    private val approved = resourceImage("approved.png")
    private val defaultAvatar = resourceImage("avatar.png")

    fun create(reviewer: PRParticipant, reviewerAvatar: BufferedImage): JLabel {
        val avatar = try { scaleImage(reviewerAvatar) } catch (e: IOException) { defaultAvatar }
        val icon = if (reviewer.approved) overlay(avatar, approved) else ImageIcon(avatar)
        val iconComponent =  JLabel(icon)
        iconComponent.toolTipText = reviewer.user.displayName
        val statusColor = ParticipantStatusColors.getColors(reviewer.status)
        iconComponent.border = BorderFactory.createLineBorder(statusColor)
        return iconComponent
    }

    private fun overlay(image: Image, overlay: Image): ImageIcon {
        val combined = UIUtil.createImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB)
        val g = combined.graphics
        g.drawImage(image, 0, 0, null)
        g.drawImage(overlay, 0, 0, null)
        return ImageIcon(combined)
    }

    private fun scaleImage(image: BufferedImage) = image.getScaledInstance(imageSize, imageSize, BufferedImage.SCALE_DEFAULT)

    private fun resourceImage(relativePath: String) = scaleImage(ImageIO.read(javaClass.classLoader.getResource(relativePath)))

}