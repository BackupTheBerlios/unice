ROOT := ..
RM := rm -rf

# Each subdirectory must contribute its source files here
C_SRCS := 
CC_SRCS := 
CXX_SRCS := 
CAPC_SRCS := 
CPP_SRCS := 

LIBS := 

USER_OBJS := 

OBJS = $(C_SRCS:$(ROOT)/%.c=%.o) $(CC_SRCS:$(ROOT)/%.cc=%.o) $(CXX_SRCS:$(ROOT)/%.cxx=%.o) $(CAPC_SRCS:$(ROOT)/%.C=%.o) $(CPP_SRCS:$(ROOT)/%.cpp=%.o)

# Every subdirectory with source files must be described here
SUBDIRS := \
src \

# Include the makefiles for each source subdirectory
-include ${patsubst %, %/subdir.mk, $(SUBDIRS)}


all: Magazzino

Magazzino: $(OBJS)
	gcc  -o $@ $(OBJS) $(USER_OBJS) $(LIBS)

clean:
	-$(RM) $(OBJS) Magazzino

.PHONY: all clean deps

# Include automatically-generated dependency list:
-include ${patsubst %, %/subdir.dep, $(SUBDIRS)}
