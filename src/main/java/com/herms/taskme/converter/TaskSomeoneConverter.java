package com.herms.taskme.converter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.herms.taskme.enums.FrequencyEnum;
import com.herms.taskme.enums.TaskState;
import com.herms.taskme.service.TaskSomeoneService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herms.taskme.dto.TaskApplicationForListDTO;
import com.herms.taskme.dto.TaskSomeoneDetailsDTO;
import com.herms.taskme.dto.UserDTO;
import com.herms.taskme.model.TaskApplication;
import com.herms.taskme.model.TaskSomeone;
import com.herms.taskme.model.User;
import com.herms.taskme.repository.UserRepository;
import com.herms.taskme.service.CustomUserDetailsService;
import com.herms.taskme.service.TaskApplicationService;

@Service
public class TaskSomeoneConverter {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private TaskApplicationService taskApplicationService;
	@Autowired
	private TaskApplicationConverter taskApplicationConverter;
	@Autowired
	private TaskSomeoneService taskSomeoneService;

	private ModelMapper modelMapper;

	public TaskSomeoneConverter() {
		this.modelMapper = new ModelMapper();
		this.modelMapper.addConverter(toTaskSomeoneDetailsDTOConv);
		this.modelMapper.addConverter(fromTaskSomeoneDetailsDTOConv);
	}

	public <T> List<TaskSomeone> fromDTO(List<T> conversationDTOsList) {
		return conversationDTOsList.stream().map(taskSomeone -> fromDTO(taskSomeone)).collect(Collectors.toList());
	}

	public <T> TaskSomeone fromDTO(T conversationDTO) {
		TaskSomeone taskSomeone = new TaskSomeone();
		modelMapper.map(conversationDTO, taskSomeone);
		return taskSomeone;
	}

	public <T> List<T> toDTO(List<TaskSomeone> taskSomeoneList, Class<T> clazz) throws Exception {
		List<T> returnDTOsList = new ArrayList<>();

		for (TaskSomeone task : taskSomeoneList) {
			returnDTOsList.add(toDTO(task, clazz));
		}
		return returnDTOsList;
	}

	/**
	 * This method is used to return any DTO of the TaskSomeone model
	 * 
	 * @param <T>         Class of the target DTO
	 * @param taskSomeone original object to be DTOrized
	 * @return The Object of class T (must be a DTO class)
	 * @throws Exception
	 */
	public <T> T toDTO(TaskSomeone taskSomeone, Class<T> clazz) {
		T dto = null;
		try {
			dto = clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			System.out.println("[ERROR] Unable to convert object of class " + taskSomeone.getClass().getName() + " to class"
					+ clazz.getName());
		}
		modelMapper.map(taskSomeone, dto);
		return dto;
	}

	Converter<TaskSomeone, TaskSomeoneDetailsDTO> toTaskSomeoneDetailsDTOConv = new Converter<TaskSomeone, TaskSomeoneDetailsDTO>() {

		@Override
		public TaskSomeoneDetailsDTO convert(MappingContext<TaskSomeone, TaskSomeoneDetailsDTO> context) {

			context.getDestination().setId(context.getSource().getId());
			context.getDestination().setCreatedOn(context.getSource().getCreatedOn());
			context.getDestination().setDescription(context.getSource().getDescription());
			context.getDestination().setLocation(context.getSource().getLocation());
			context.getDestination().setTitle(context.getSource().getTitle());
			context.getDestination().setUser(new UserDTO(context.getSource().getUser()));
			context.getDestination().setFrequency(!Objects.isNull(context.getSource().getFrequency())
							? context.getSource().getFrequency().getCode()
							: null);
			context.getDestination().setStartDate(context.getSource().getStartDate());
			context.getDestination().setEndDate(context.getSource().getEndDate());
			context.getDestination().setState(!Objects.isNull(context.getSource().getState())
																		? context.getSource().getState().getCode()
																		: null);
			context.getDestination().setNextState(!Objects.isNull(context.getSource().getState())
					? taskSomeoneService.getNextTaskState(context.getSource().getState().getCode())
					: null);
			context.getDestination().setPreviousState(!Objects.isNull(context.getSource().getState())
					? taskSomeoneService.getPreviousTaskState(context.getSource().getState().getCode())
					: null);
			context.getDestination().setMediaList(context.getSource().getMediaList());
			User principal = customUserDetailsService.getLoggedUser();
			context.getDestination().setOwnTask(principal.getId().equals(context.getSource().getUser().getId()));
			
			List<TaskApplication> applicantions = taskApplicationService.getAllTaskApplicationByTaskId(context.getDestination().getId());
			List<TaskApplicationForListDTO> applicantionsDTO = taskApplicationConverter.toDTO(applicantions, TaskApplicationForListDTO.class);
			context.getDestination().setTaskApplicants(applicantionsDTO);
			boolean alreadyApplied = applicantionsDTO.stream().anyMatch((application) -> application.getUser().getId().equals(principal.getId()));
			context.getDestination().setAlreadyApplied(alreadyApplied);
			List<User> participants = userRepository.findAllUsersParticipatingInTask(context.getSource().getId());
			context.getDestination().setParticipants(participants.stream().map(p -> new UserDTO(p)).collect(Collectors.toList()));
			return context.getDestination();
		}
	};

	Converter<TaskSomeoneDetailsDTO, TaskSomeone> fromTaskSomeoneDetailsDTOConv = new Converter<TaskSomeoneDetailsDTO, TaskSomeone>() {

		@Override
		public TaskSomeone convert(MappingContext<TaskSomeoneDetailsDTO, TaskSomeone> context) {

			context.getDestination().setId(context.getSource().getId());
			context.getDestination().setCreatedOn(context.getSource().getCreatedOn());
			context.getDestination().setDescription(context.getSource().getDescription());
			context.getDestination().setLocation(context.getSource().getLocation());
			context.getDestination().setTitle(context.getSource().getTitle());
			context.getDestination().setFrequency(FrequencyEnum.toEnum(context.getSource().getFrequency()));
			context.getDestination().setStartDate(context.getSource().getStartDate());
			context.getDestination().setEndDate(context.getSource().getEndDate());
			context.getDestination().setState(TaskState.toEnum(context.getSource().getState()));
			context.getDestination().setUser(userRepository.getOne(context.getSource().getUser().getId()));

			return context.getDestination();
		}
	};
}
