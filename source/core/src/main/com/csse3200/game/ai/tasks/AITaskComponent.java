package com.csse3200.game.ai.tasks;

import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Task-based AI component. Given a list of tasks with priorities, the AI component will run the
 * highest priority task each frame. Tasks can be made up of smaller sub-tasks. A negative priority
 * indicates that the task should not be run.
 *
 * <p>This is a simple implementation of Goal-Oriented Action Planning (GOAP), a common AI decision
 * algorithm in games that's more powerful than Finite State Machines (FSMs) (State pattern).
 */
public class AITaskComponent extends Component implements TaskRunner {
  private static final Logger logger = LoggerFactory.getLogger(AITaskComponent.class);
  /** List storing tasks of AI entity. */
  private final List<PriorityTask> priorityTasks = new ArrayList<>(2);
  /** Task currently running. */
  private PriorityTask currentTask;

  /**
   * When component is created, create all tasks.
   */
  @Override
  public void create() {
    for (PriorityTask task : priorityTasks) {
      task.create(this);
    }
  }

  /**
   * Add a priority task to the list of tasks. This task will be run only when it has the highest
   * priority, and can be stopped to run a higher priority task.
   *
   * @param task Task to add
   * @return self
   */
  public AITaskComponent addTask(PriorityTask task) {
    logger.debug("{} Adding task {}", this, task);
    priorityTasks.add(task);

    return this;
  }

  /**
   * On update, run the current highest priority task. If it's a different one, stop the old one and
   * start the new one. If the highest priority task has negative priority, no task will be run.
   */
  @Override
  public void update() {
    PriorityTask desiredtask = getHighestPriorityTask();
    if (desiredtask == null || desiredtask.getPriority() < 0) {
      return;
    }

    if (desiredtask != currentTask) {
      changeTask(desiredtask);
    }
    currentTask.update();
  }

  /**
   * Dispose AITaskComponent.
   */
  @Override
  public void dispose() {
    if (currentTask != null) {
      currentTask.stop();
    }
  }

  /**
   * Return highest priority task.
   * @return highest priority task.
   */
  private PriorityTask getHighestPriorityTask() {
    try {
      return Collections.max(priorityTasks, Comparator.comparingInt(PriorityTask::getPriority));
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  /**
   * Changed current task to desired task
   * @param desiredTask task to change to
   */
  private void changeTask(PriorityTask desiredTask) {
    logger.debug("{} Changing to task {}", this, desiredTask);
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = desiredTask;
    if (desiredTask != null) {
      desiredTask.start();
    }
  }
}
