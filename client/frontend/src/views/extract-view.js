import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-upload/src/vaadin-upload.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';

class ExtractView extends PolymerElement {

    static get template() {
        return html`
<style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                }
            </style>
<vaadin-button id="backToHomePageButton">
 <iron-icon icon="lumo:edit" slot="prefix"></iron-icon>Back to Home Page 
</vaadin-button>
<vaadin-vertical-layout style="width: 100%; height: 100%;">
 <label style="flex-shrink: 1; align-self: center; font-size: 500%">Extract Secret File</label>
 <vaadin-upload id="stegoFileUpload" style="align-self: center;"></vaadin-upload>
 <vaadin-button theme="primary" id="submitButton" style="align-self: center; flex-grow: 0; margin-top: 5%;">
   Submit 
 </vaadin-button>
 <a href="default.txt" id="downloadSecretFileAnchor" style="align-self: center;">
  <vaadin-button theme="primary success" id="downloadSecretFileButton" style="align-self: center;">
    Download Secret File 
  </vaadin-button></a>
 <vaadin-button theme="primary" id="retryButton" style="align-self: center;">
   Primary 
 </vaadin-button>
</vaadin-vertical-layout>
`;
    }

    static get is() {
        return 'extract-view';
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(ExtractView.is, ExtractView);
