# Each subdirectory must contribute its source files here
C_SRCS += \
${addprefix $(ROOT)/src/,\
base_dati.c \
dati_disco.c \
dati_memoria.c \
lock.c \
test.c \
test_disco.c \
test_lock.c \
tree.c \
}

CC_SRCS += \
${addprefix $(ROOT)/src/,\
}

CXX_SRCS += \
${addprefix $(ROOT)/src/,\
}

CAPC_SRCS += \
${addprefix $(ROOT)/src/,\
}

CPP_SRCS += \
${addprefix $(ROOT)/src/,\
}

# Each subdirectory must supply rules for building sources it contributes
src/%.o: $(ROOT)/src/%.c
	gcc -O3 -Wall -c -o $@ $<


