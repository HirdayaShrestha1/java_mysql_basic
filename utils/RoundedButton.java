package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JLabel {
    private Color backgroundColor;
    private Color textColor;
    private Color baseBackgroundColor;
    private Color baseTextColor;
    private final Color hoverBackgroundColor;
    private final Color hoverTextColor;
    private Runnable onClick;

    public RoundedButton(String text, Color backgroundColor, Color textColor, Color hoverBgColor, Color hoverFgColor) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.baseBackgroundColor = backgroundColor;
        this.baseTextColor = textColor;
        this.hoverBackgroundColor = hoverBgColor;
        this.hoverTextColor = hoverFgColor;
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(UIUtils.FONT_GENERAL_UI);
        setForeground(textColor);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (onClick != null) {
                    onClick.run();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                RoundedButton.this.backgroundColor = RoundedButton.this.hoverBackgroundColor;
                RoundedButton.this.textColor = RoundedButton.this.hoverTextColor;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                RoundedButton.this.backgroundColor = baseBackgroundColor;
                RoundedButton.this.textColor = baseTextColor;
                repaint();
            }
        });
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public void setColors(Color backgroundColor, Color textColor) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.baseBackgroundColor = backgroundColor;
        this.baseTextColor = textColor;
        setForeground(textColor);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = UIUtils.get2dGraphics(g);
        super.paintComponent(g2);

        Insets insets = getInsets();
        int w = getWidth() - insets.left - insets.right;
        int h = getHeight() - insets.top - insets.bottom;
        g2.setColor(backgroundColor);
        g2.fillRoundRect(insets.left, insets.top, w, h, UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);

        FontMetrics metrics = g2.getFontMetrics(getFont());
        int x2 = (getWidth() - metrics.stringWidth(getText())) / 2;
        int y2 = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.setColor(textColor);
        g2.drawString(getText(), x2, y2);
    }
}
