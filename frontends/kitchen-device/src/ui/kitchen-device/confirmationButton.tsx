import React from "react";
import { Button, ButtonProps, Popup, SemanticCOLORS } from "semantic-ui-react";

export default class ConfirmationButton extends React.Component<Parameters, State> {
    private static readonly EMPTY_STATE = {
        popupOpen: false,
        descriptionOpen: false
    };

    private static readonly TIMEOUT_LENGTH = 2500;
    togglePopup = () => {
        if (this.state.popupOpen) {
            clearTimeout(this.timer);
        } else {
            this.timer = setTimeout(() => this.setState({ popupOpen: false }), ConfirmationButton.TIMEOUT_LENGTH);
        }
        this.setState({ popupOpen: !this.state.popupOpen });
    };
    setPopupOwner = (reference: any) => this.setState({ buttonReference: reference });
    private timer: any = null;

    constructor(parameters: Parameters) {
        super(parameters);
        this.state = ConfirmationButton.EMPTY_STATE;
    }

    render(): React.ReactNode {
        return <Popup context={this.state.buttonReference}
                      content={<Button color={this.props.popupButtonColor}
                                       content={this.props.popupButtonContent}
                                       onClick={this.props.onClick}/>
                      }
                      position='top center'
                      open={this.state.popupOpen}
                      on='click'
                      hideOnScroll
                      trigger={
                          <Button color={this.props.openerColor}
                                  onClick={this.togglePopup}><span
                              ref={this.setPopupOwner}>{this.props.openerContent}</span>
                          </Button>
                      }
        />
    }
}

interface State {
    popupOpen: boolean,
    descriptionOpen: boolean,
    buttonReference?: object
}

interface Parameters {
    openerContent: string,
    popupButtonContent: any,
    onClick: (event: React.MouseEvent<HTMLButtonElement>, data: ButtonProps) => void,
    openerColor?: SemanticCOLORS,
    popupButtonColor?: SemanticCOLORS
}