#version 120

uniform float red; // uniform variables can be shared with the program
uniform float green;
uniform float blue;
uniform float alpha;

void main() {
gl_FragColor = vec4(red, green, blue, alpha);
}