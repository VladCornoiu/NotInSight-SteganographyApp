import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';

class MainView extends PolymerElement {

    static get template() {
        return html`
<style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                }
            </style>
<vaadin-horizontal-layout style="width: 100%; height: 100%; background-image: url('https://www.pandasecurity.com/mediacenter/src/uploads/2017/12/esteganografia.jpg');">
 <vaadin-horizontal-layout style="width: 40%; height: 100%; flex-shrink: 0;" theme="spacing">
  <h1 style="color:white; flex-shrink: 0; flex-grow: 0; align-self: center; margin-left: 20%; font-size: 40px; background-color: rgba(0, 0, 0, 0.6);">NOTINSIGHT<br>STEGANOGRAPHY APPLICATION</h1>
 </vaadin-horizontal-layout>
 <vaadin-horizontal-layout style="width: 60%; flex-shrink: 0; height: 100%;">
  <vaadin-button theme="primary" style="color:white; width: 20%; flex-shrink: 0; flex-grow: 1; height: 8%; align-self: center; margin-right: 20px; margin-left: 10%;" id="EmbedButton">
    Embed Secret 
  </vaadin-button>
  <vaadin-button style="color:white; width: 20%; height: 8%; flex-shrink: 0; flex-grow: 1; align-self: center; margin-right: 10%; margin-left: 20px;" theme="primary" id="ExtractButton">
    Extract Secret 
  </vaadin-button>
 </vaadin-horizontal-layout>
</vaadin-horizontal-layout>
`;
    }

    static get is() {
        return 'main-view';
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(MainView.is, MainView);
