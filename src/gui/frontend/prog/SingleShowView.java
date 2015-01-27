package gui.frontend.prog;

import engine.Show;
import engine.custom_managers.ShowManager;
import gui.frontend.buy_process.ShowDetailsFrame;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * This is Single show panel, part of global UI
 */
public class SingleShowView extends JPanel {
    private ShowManager showManager;
    private Show myShow;
    private ProgTab context;
    JButton btnDetails = new JButton("Dettagli");

    public SingleShowView(ProgTab context, Show myShow, ShowManager showManager) {
        this.context = context;
        this.myShow = myShow;
        this.showManager = showManager;
        initLayout();
        initListeners();
    }

    private void initListeners() {
        btnDetails.addActionListener(e -> {
            new ShowDetailsFrame(myShow, context.getClient()).setVisible(true);
        });
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.gray));
        setPreferredSize(new Dimension(130, 220));
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        add(imagePanel, BorderLayout.NORTH);

        JLabel filmPicture = new JLabel();
        filmPicture.setIcon(myShow.getFilm().getImg());
        imagePanel.add(filmPicture);

        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

        JLabel lbTitle = new JLabel(myShow.getFilm().getTitle());
        lbTitle.setFont(new Font("Tahoma", Font.BOLD, 15));
        lbTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        dataPanel.add(lbTitle);

        JLabel lbYear = new JLabel("" + myShow.getFilm().getYear());
        lbYear.setAlignmentX(Component.CENTER_ALIGNMENT);
        dataPanel.add(lbYear);

        JLabel lbTime = new JLabel("Inizio: " + myShow.getDate().format(DateTimeFormatter.ofPattern("HH:mm")));
        lbTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        dataPanel.add(lbTime);

        JLabel lbSala = new JLabel("Sala: " + (myShow.getHall().getNumber() + 1));
        lbSala.setAlignmentX(Component.CENTER_ALIGNMENT);
        dataPanel.add(lbSala);

        add(dataPanel, BorderLayout.CENTER);
        add(btnDetails, BorderLayout.SOUTH);

    }
}
