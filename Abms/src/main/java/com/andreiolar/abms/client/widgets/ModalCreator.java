package com.andreiolar.abms.client.widgets;

import com.google.gwt.user.client.ui.RootPanel;

import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.CollapsibleType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.ModalType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialModalContent;
import gwt.material.design.client.ui.MaterialModalFooter;
import gwt.material.design.client.ui.MaterialTitle;
import gwt.material.design.client.ui.html.Br;

public final class ModalCreator {

	public static MaterialModal createModal(Throwable caught) {
		return createModal("Something went wrong", caught);
	}

	public static MaterialModal createModal(String title, Throwable caught) {
		MaterialModal materialModal = new MaterialModal();
		materialModal.setType(ModalType.DEFAULT);
		materialModal.setDismissible(false);
		materialModal.setInDuration(500);
		materialModal.setOutDuration(500);

		MaterialModalContent materialModalContent = new MaterialModalContent();
		MaterialTitle materialTitle = new MaterialTitle(title);
		materialTitle.setDescription(caught.getMessage());

		materialModalContent.add(materialTitle);

		// Collapsible for extra information
		MaterialCollapsible materialCollapsible = new MaterialCollapsible();
		materialCollapsible.setType(CollapsibleType.POPOUT);
		materialCollapsible.add(new Br());

		MaterialCollapsibleItem materialCollapsibleItem = new MaterialCollapsibleItem();

		MaterialCollapsibleHeader materialCollapsibleHeader = new MaterialCollapsibleHeader();
		materialCollapsibleHeader.setBackgroundColor(Color.GREY);
		materialCollapsibleHeader.setWaves(WavesType.DEFAULT);

		MaterialLink materialLink = new MaterialLink();
		materialLink.setText("Show More Details");
		materialLink.setIconType(IconType.ADD_CIRCLE_OUTLINE);
		materialLink.setIconPosition(IconPosition.LEFT);
		materialLink.setTextColor(Color.WHITE);

		materialCollapsibleHeader.addClickHandler(h -> {
			IconType iconType = materialLink.getIcon().getIconType();

			if (IconType.ADD_CIRCLE_OUTLINE == iconType) {
				materialLink.setIconType(IconType.REMOVE_CIRCLE_OUTLINE);
			} else {
				materialLink.setIconType(IconType.ADD_CIRCLE_OUTLINE);
			}

		});

		materialCollapsibleHeader.add(materialLink);

		MaterialCollapsibleBody materialCollapsibleBody = new MaterialCollapsibleBody();
		materialCollapsibleBody.setBackgroundColor(Color.GREY_DARKEN_1);

		MaterialLabel materialLabel = new MaterialLabel();
		materialLabel.setTextColor(Color.WHITE);
		materialLabel.setText(getMessage(caught));

		materialCollapsibleBody.add(materialLabel);
		materialCollapsibleItem.add(materialCollapsibleHeader);
		materialCollapsibleItem.add(materialCollapsibleBody);

		materialCollapsible.add(materialCollapsibleItem);
		materialModalContent.add(materialCollapsible);

		MaterialModalFooter materialModalFooter = new MaterialModalFooter();
		MaterialButton closeButton = new MaterialButton();
		closeButton.setText("Close");
		closeButton.setType(ButtonType.FLAT);
		closeButton.addClickHandler(h -> {
			materialModal.close();
			RootPanel.get().remove(materialModal);
		});

		materialModalFooter.add(closeButton);
		materialModal.add(materialModalContent);
		materialModal.add(materialModalFooter);

		return materialModal;
	}

	private static String getMessage(Throwable throwable) {
		// StackTraceDeobfuscator stackTraceDeobfuscator = StackTraceDeobfuscator.fromFileSystem("WEB-INF/deploy/abms/symbolMaps/");
		// StackTraceElement[] resymbolized = stackTraceDeobfuscator.resymbolize(throwable.getStackTrace(), "871AFA126773584834214DADC2701C92");

		StringBuilder sb = new StringBuilder();

		for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
			sb.append(stackTraceElement + "\n");
		}

		return sb.toString();
	}

}
