## Description

This program is a simulator (numerical solver) for three types of differential equations, the heat equation, the wave equation and the wave equation with damping. By considering the case with one spatial variable, the program creates a Hermite spline interpolation from user inputted vertices. It then models the numerical solution (e.g. heat distribution, wave) and visualizes the results using Fourier series coefficients.

## Preview

### Heat Equation
<img src="assets/preview-heat.gif" alt="Alt Text" width="400" height="350" />

Visualizing heat distribution over time.

### Wave Equation
<img src="assets/preview-wave.gif" alt="Alt Text" width="400" height="350" />

Observing wave propagation over time.

### Wave Equation with Dampening
<img src="assets/preview-wavedamp.gif" alt="Alt Text" width="400" height="350" />

Seeing how damping influences wave behavior over time.

## Assumptions

For the heat equation, we assume the spatial variable $x$ goes from $0$ to $L$ where $L>0$ is defined as the upper bound of the domain. Let $u(x,t)$ be the temperature at position $x$ at time $t$. The boundary conditions are given by

$$u(0,t) = u(L,t) = 0\text{, for all } t\ge0.$$

Similarly, for the wave eqaution, we assume the spatial variable $x$ goes from $0$ to $L$ where $L$ is defiend as in the heat equation. Letting $v(x,t)$ be the spatial displacement from the origin at position $x$ at time $t$ we have the boundary conditions given by
$$v(0,t) = v(L,t) = 0\text{, for all } t\ge0$$
and
$$\frac{dv}{dt}v(x,0)=0, \text{ where } 0\leq x\leq L.$$

## Implementations

### Hermite Spline Interpolation

The program uses Hermite spline interpolation to create a smooth curve through a series of user-defined points. Each segment between points is interpolated using cubic polynomials, this allows for continuity and that the boundary conditions are met, for the heat equation, this means that the temperatures are zero and for the wave equation we have a fixed zero displacement.

### Fourier Series Method

The differential equations are solved using Fourier series representations, where the solutions are expressed as a sum of sine functions with coefficients determined by the initial polynomial from the spline interpolation. The program calculates these coefficients and uses them to simulate the evolution of the differential equations over time.

### Interface: Simulation
Classes implementing this interface ('HeatSimulation', 'WaveSimulation', 'WaveDampingSimulation') are meant to setup the simulation by constructing the polynomial from the Hermite spline interoploation and derive the corresponding Fourier coefficients. Thereafter it will put this information in a Graph object that is now ready for simulating the corresponding differential equation. 

### Interface: Graph
Classes implementing this interface ('GraphHeat', 'GraphWave', 'GraphWaveDamping') represents the solution of the their corresponding differential equation using the Fourier series coefficients. By supplementing a given time, the graph can update its vertices (points (x,y) representing the fourier series function) for visualization.

## Dependencies

The java code relies on the javax.swing package for graphical rendering and requires the EJML Java library (version 0.43) for matrix calculations.

## Further Information
For a more in-depth look at this project, please refer to [mariusnaasen.com/projects/heat-wave-equation-simulator](https://mariusnaasen.com/projects/heat-wave-equation-simulator).

## Author
Marius H. Naasen, originally created August 2021.
