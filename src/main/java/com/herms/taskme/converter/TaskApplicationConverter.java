package com.herms.taskme.converter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herms.taskme.dto.TaskApplicationDetailsDTO;
import com.herms.taskme.dto.UserDTO;
import com.herms.taskme.model.TaskApplication;
import com.herms.taskme.model.User;
import com.herms.taskme.repository.UserRepository;
import com.herms.taskme.service.CustomUserDetailsService;

@Service
public class TaskApplicationConverter {
	@Autowired
	private UserRepository userRepository;
	@Autowired
    private CustomUserDetailsService customUserDetailsService;
	
	private ModelMapper modelMapper;

	public TaskApplicationConverter() {
		this.modelMapper = new ModelMapper();
		this.modelMapper.addConverter(toTaskApplicationDetailsDTOConv);
		this.modelMapper.addConverter(fromTaskApplicationDetailsDTOConv);
	}

	public <T> List<TaskApplication> fromDTO(List<T> conversationDTOsList) {
		return conversationDTOsList.stream().map(taskSomeone -> fromDTO(taskSomeone)).collect(Collectors.toList());
	}

	public <T> TaskApplication fromDTO(T conversationDTO) {
		TaskApplication taskSomeone = new TaskApplication();
		modelMapper.map(conversationDTO, taskSomeone);
		return taskSomeone;
	}

	public <T> List<T> toDTO(List<TaskApplication> taskSomeoneList, Class<T> clazz) throws Exception {
		List<T> returnDTOsList = new ArrayList<>();

		for (TaskApplication task : taskSomeoneList) {
			returnDTOsList.add(toDTO(task, clazz));
		}
		return returnDTOsList;
	}

	/**
	 * This method is used to return any DTO of the TaskApplication model
	 * 
	 * @param <T>         Class of the target DTO
	 * @param taskSomeone original object to be DTOrized
	 * @return The Object of class T (must be a DTO class)
	 * @throws Exception
	 */
	public <T> T toDTO(TaskApplication taskSomeone, Class<T> clazz) throws Exception {
		T dto;
		try {
			dto = clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new Exception("Unable to convert object of class " + taskSomeone.getClass().getName() + " to class"
					+ clazz.getName());
		}
		modelMapper.map(taskSomeone, dto);
		return dto;
	}

	Converter<TaskApplication, TaskApplicationDetailsDTO> toTaskApplicationDetailsDTOConv = new Converter<TaskApplication, TaskApplicationDetailsDTO>() {

		@Override
		public TaskApplicationDetailsDTO convert(MappingContext<TaskApplication, TaskApplicationDetailsDTO> context) {


			return context.getDestination();
		}
	};

	Converter<TaskApplicationDetailsDTO, TaskApplication> fromTaskApplicationDetailsDTOConv = new Converter<TaskApplicationDetailsDTO, TaskApplication>() {

		@Override
		public TaskApplication convert(MappingContext<TaskApplicationDetailsDTO, TaskApplication> context) {
			

			return context.getDestination();
		}
	};
}
