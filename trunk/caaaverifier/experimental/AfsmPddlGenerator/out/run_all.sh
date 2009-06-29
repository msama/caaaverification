#!/bin/sh

PLANNER=/home/rax/Programs/sgplan522/sgplan522

BASE_FOLDER=powermanager
DOMAIN=$BASE_FOLDER/domain.pddl

echo 'Using domain ' $DOMAIN

for FILE in $BASE_FOLDER/in-state-violation/*.pddl
do
  echo 'Processing' $FILE
  RESULT=$FILE.result
  ($PLANNER -o $DOMAIN -f $FILE -out $RESULT) | grep "@£$%^&*()"
  # Each reached goal is a fault
done;

for FILE in $BASE_FOLDER/nondeterministic-activations/*.pddl
do
  echo 'Processing' $FILE
  RESULT=$FILE.result
  ($PLANNER -o $DOMAIN -f $FILE -out $RESULT) | grep "@£$%^&*()"
  # Each reached goal is a fault
done;

for FILE in $BASE_FOLDER/dead-states/*.pddl
do
  echo 'Processing' $FILE
  RESULT=$FILE.result
  ($PLANNER -o $DOMAIN -f $FILE -out $RESULT) | grep "@£$%^&*()"
  # Each missed goal is a fault
done;

for FILE in $BASE_FOLDER/dead-rules/*.pddl
do
  echo 'Processing' $FILE
  RESULT=$FILE.result
  ($PLANNER -o $DOMAIN -f $FILE -out $RESULT) | grep "@£$%^&*()"
  # Each missed goal is a fault
done;
