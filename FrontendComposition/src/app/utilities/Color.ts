import * as convert from 'color-convert';
import * as colorNames from 'color-name';
import * as validate from './Validate';

/**
 * Defines the properties of a color domain object.
 *
 * This implementation works internally with the hue, saturation and lightness model. This
 * model allows simple, jet effective manipulation.
 */
export default class Color {
    static fromName(name : keyof typeof colorNames) : Color {
        let hsl = convert.keyword.hsl(name);
        return new Color(hsl[0], hsl[1], hsl[2]);
    }

    static fromHsv(...hsv : [number, number, number]) : Color {
        let hsl = convert.hsv.hsl(hsv);
        return new Color(hsl[0], hsl[1], hsl[2]);
    }

    static fromHwb(...hsv : [number, number, number]) : Color {
        let hsl = convert.hwb.hsl(hsv);
        return new Color(hsl[0], hsl[1], hsl[2]);
    }

    static fromCmyk(...cmyk : [number, number, number, number]) : Color {
        let hsl = convert.cmyk.hsl(cmyk);
        return new Color(hsl[0], hsl[1], hsl[2]);
    }

    static fromAnsi16(ansi : number) : Color {
        let hsl = convert.ansi16.hsl(ansi);
        return new Color(hsl[0], hsl[1], hsl[2]);
    }

    static fromAnsi256(ansi : number) : Color {
        let hsl = convert.ansi256.hsl(ansi);
        return new Color(hsl[0], hsl[1], hsl[2]);
    }

    static fromHex(hex : string) : Color {
        let hsl = convert.hex.hsl(hex);
        return new Color(hsl[0], hsl[1], hsl[2]);
    }

    private h;
    private s;
    private l;

    constructor(hue : number, saturation : number, lightness : number) {
        validate.positive(hue);
        this.h = hue;
        this.s = saturation;
        this.l = lightness;
    }

    get hue() {
        return this.h;
    }

    get saturation() {
        return this.s;
    }

    get lightness() {
        return this.l;
    }

    toRgb() : [number, number, number] {
        return convert.hsl.rgb([this.h, this.s, this.l]);
    }

    toString() : string {
        return JSON.stringify(this);
    }

    toName() : string {
        return convert.hsl.keyword([this.h, this.s, this.l]);
    }

    toHsl() : [number, number, number] {
        return [this.h, this.s, this.l];
    }

    toHsv() : [number, number, number] {
        return convert.hsl.hsv([this.h, this.s, this.l]);
    }

    toHwb() : [number, number, number] {
        return convert.hsl.hwb([this.h, this.s, this.l]);
    }

    toCmyk() : [number, number, number, number] {
        return convert.hsl.cmyk([this.h, this.s, this.l]);
    }

    toAnsi16() : number {
        return convert.hsl.ansi16([this.h, this.s, this.l]);
    }

    toAnsi256() : number {
        return convert.hsl.ansi256([this.h, this.s, this.l]);
    }

    toHex() : string {
        return convert.hsl.hex([this.h, this.s, this.l]);
    }
}
