Matthew 2/9/18 11:33pm
======================

I just built a maven project in this directory so that we can use it to manage dependencies.

I am going to next try to get maven to include cucumber and JSEF.

JSEF is an api which will hopefully let us read easily from a CSV into any arbitrary Java object.

The plan is to read these objects from the file and shove them into an sql database. I vote we make John M. be in charge of the SQL.

I am of the opinion that it would be wise to assign some of the positions described in chapter two of the Agile textbook. 

We should start next week as our first iteration, and strive to have iterations either weekly or biweekly. I am open to input. 

Proposed schedule:
------------------
+ Iteration 1: Read CSV file (using JSEF?) and put it into SQL. Have basic queries working.
                Possible test cases/user stories include giving a couple of class records and reading back the number of students registered for a class.
+ Iteration 2: Process data/build SQL queries to confirm that all students registered have completed all prereqs satisfactorily. 
                Test cases should validate the full range of expected features.

If you would rather divide this further and can think of a reasonable way to do so, I'm open to the idea.

Anyway, expect another update later tonight when I get JSEF working.


Matthew 2/10/2018 4:00pm
========================

I have made some progress with Maven. I regenerated the project and have lightly
configured the pom.xml to include Cucumber and JSEFA dependencies. I also added
a .gitignore to the root directory of the repo and told it to ignore anything
in /prereqs/target/ (which where maven puts the compiled project) and anything
in .idea/ (which is where IntelliJ stores project specific settings).

Maven comes with integration for junit, so we have to use the junit runner for
cucumber to leverage the power of cucumber features.
See the [cucumber.io](https://cucumber.io/docs/reference/jvm#junit-runner)
website for documentation on how to use the junit runner with cucumber.

I am sure there are slightly different preferred ways of doing all of this that
leverage features of IntelliJ, but IDEs scare me.
Also, I believe there is more value in learning the underlying framework than in
memorizing IDE shortcuts, so I move that we try to stay as lightweight and
foundational as we can..

Regarding Cucumber, we may want to investigate adding a "dependency injection"
module. Because Cucumber creates new instances of each of the step definition
classes every time a scenario starts, you are limited in your ability to
remember/share information between steps without static variables. These modules
help you overcome this. See [this](https://cucumber.io/docs/reference/java-di)
page for details about several available options. 

I further propose that from this point on, we enforce better git practice. No
commiting straight to `master` when not necessary, use of feature branches,
granular commits, etc.
See [this](https://www.atlassian.com/git/tutorials/comparing-workflows/feature
-branch-workflow) page for information on "feature branch workflow" and
[this](https://www.git-tower.com/learn/git/ebook/en/command-line/appendix/best
-practices) for the source of the following "best practices".

### Git Best Practices (Soon to be our practices!)
1. Commit Related Changes
2. Commit Often
3. Don't Commit Half-Done Work
4. Test BEFORE You Commit
5. Write Good Commit Messages
6. Version Control is not Just a Backup System
7. Use Branches
8. Use *Feature Branching* Workflow

Matthew 2/10/2018 4:55pm
========================

In an effort to practice the Feature Branching Workflow, I have added two new 
branches: `v0.1` and `cukeFeatures`. `v0.1` will be the branch we merge into
as we work on the first iteration, and when the first iteration is complete
we will merge it into `master`. `cukeFeatures` is the branch where I will write
the first draft of our Cucumber Features file. 

**The devJournal in the `master` branch will not be updated until the first
iteration is merged in.** Check the devJournal in the `v0.1` branch for current
status.




*[JSEF]: Java Simple Exchange Format
