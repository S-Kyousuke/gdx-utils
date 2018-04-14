package com.github.skyousuke.gdxutils.batchmonitor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pools;

class RecordsTable extends Table {

    private static final int GRAPH_WIDTH = 8;
    private static final int GRAPH_HEIGHT = 30;

    private final Label label;
    private final Label renderCallsTooltipLabel;
    private final Label batchTooltipLabel;
    private final RenderCallsGraph graph;
    private final RecordsImageTable recordsImageTable;

    RecordsTable() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Assets.instance.font;
        labelStyle.fontColor = Color.BLACK;

        TextTooltip.TextTooltipStyle tooltipStyle = new TextTooltip.TextTooltipStyle();
        Label.LabelStyle tooltipLabelStyle = new Label.LabelStyle(labelStyle);
        tooltipLabelStyle.font = Assets.instance.font;
        tooltipLabelStyle.fontColor = Color.WHITE;
        tooltipStyle.label = tooltipLabelStyle;
        tooltipStyle.background = Assets.instance.white.tint(Color.valueOf("333333aa"));

        TextTooltip renderCallsTooltip = new TextTooltip("", tooltipStyle);
        renderCallsTooltip.setInstant(true);
        renderCallsTooltipLabel = renderCallsTooltip.getActor();

        TextTooltip batchTooltip = new TextTooltip("", tooltipStyle);
        batchTooltip.getActor().setWrap(false);
        batchTooltip.setInstant(true);
        batchTooltipLabel = batchTooltip.getActor();

        label = new Label("", labelStyle);
        label.setAlignment(Align.center);
        label.addListener(batchTooltip);

        graph = new RenderCallsGraph();
        graph.addListener(renderCallsTooltip);

        recordsImageTable = new RecordsImageTable();

        setBackground(Assets.instance.white.tint(Color.valueOf("ccccccff")));

        row().bottom();
        add(graph).size(GRAPH_WIDTH, GRAPH_HEIGHT);
        add(label).center().width(100);
        add(recordsImageTable).padRight(-20);
        pad(0);
        setSize(getPrefWidth(), getPrefHeight());
    }

    public void init(int rank, int totalRenderCalls, BatchRecordContainer batchRecordContainer) {
        graph.init(batchRecordContainer.getRenderCalls(), totalRenderCalls, GRAPH_WIDTH, GRAPH_HEIGHT);
        label.setText("#" + rank + '\n' + batchRecordContainer.getRenderCalls() + " Render Calls");
        renderCallsTooltipLabel.setText("" + batchRecordContainer.getRenderCalls() + '/' + totalRenderCalls + " Render Calls");
        batchTooltipLabel.setText(batchRecordContainer.label);
        recordsImageTable.init(batchRecordContainer.records);
    }

    @Override
    public boolean remove() {
        if (super.remove()) {
            Pools.free(this);
            return true;
        }
        return false;
    }
}


