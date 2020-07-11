import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-upload/src/vaadin-upload.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@polymer/iron-icon/iron-icon.js';

class EmbedView extends PolymerElement {

    static get template() {
        return html`
<style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                }
            </style>
<vaadin-vertical-layout style="width: 100%; height: 100%;">
 <vaadin-button id="backToHomePageButton">
  <iron-icon icon="lumo:edit" slot="prefix"></iron-icon>Back to Home Page
 </vaadin-button>
 <label style="flex-shrink: 1; align-self: center; font-size: 500%">Embed Secret File</label>
 <vaadin-horizontal-layout style="width: 100%; height: 30%; justify-content: center;">
  <vaadin-upload id="coverFileUpload" style="margin-right: 20px;"></vaadin-upload>
  <vaadin-upload id="secretFileUpload" style="margin-left: 20px;"></vaadin-upload>
 </vaadin-horizontal-layout>
 <vaadin-button theme="primary" id="submitButton" style="align-self: center; margin-top: 50px;">
  Submit
 </vaadin-button>
 <a href="https://vaadin.com" id="downloadStegoFileAnchor" style="align-self: center;">
  <vaadin-button theme="primary success" id="downloadStegoFileButton">
   Download Stego File
  </vaadin-button></a>
</vaadin-vertical-layout>
`;
    }

    static get is() {
        return 'embed-view';
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(EmbedView.is, EmbedView);
