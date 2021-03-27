# Repository Summary

This repository contains code for risk assessment of a banks assets. We calculate the probability that given loans default and the loss that a bank would incur. We perform this operation in a monte-carlo simulation, calculating defaulting probability and the resulting losses multiple time (in the order of 10^6). The outcome is a table consisting of counts of simulations where the bank had defaults in a specific range.

*The runtime of this program has been improved through parallelisation (in the order of 10^1 - 10^2). Further improvements can be made when optimizing the code to run on a GPU (another improvement in the order of ~10^2).
