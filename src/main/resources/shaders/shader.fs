#version 120

uniform float red; // uniform variables can be shared with the program
uniform float green; // uniform variables can be shared with the program
uniform float blue; // uniform variables can be shared with the program

void main() {
gl_FragColor = vec4(red, green, blue, 1);
}