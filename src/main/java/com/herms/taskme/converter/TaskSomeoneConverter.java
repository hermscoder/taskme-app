package com.herms.taskme.converter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import com.herms.taskme.dto.TaskSomeoneDetailsDTO;
import com.herms.taskme.dto.UserDTO;
import com.herms.taskme.model.Message;
import com.herms.taskme.model.TaskSomeone;

public class TaskSomeoneConverter {
	
	private ModelMapper modelMapper;
	
	public TaskSomeoneConverter() {
		this.modelMapper = new ModelMapper();
		this.modelMapper.addConverter(toTaskSomeoneDetailsDTOConv);
		this.modelMapper.addConverter(fromTaskSomeoneDetailsDTOConv);
	}

	public <T> List<TaskSomeone> fromDTO(List<T> conversationDTOsList) {
		return conversationDTOsList.stream()
 				.map(taskSomeone -> fromDTO(taskSomeone)).collect(Collectors.toList());
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
     * @param <T> Class of the target DTO
     * @param taskSomeone original object to be DTOrized
     * @return The Object of class T (must be a DTO class)
     * @throws Exception 
     */
    public <T> T toDTO(TaskSomeone taskSomeone, Class<T> clazz) throws Exception {
    	T dto;
		try {
			dto = clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | 
				InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new Exception("Unable to convert object of class " + taskSomeone.getClass().getName() + " to class" + clazz.getName());
		}
    	modelMapper.map(taskSomeone, dto);
    	return dto;
    }

	Converter<TaskSomeone, TaskSomeoneDetailsDTO> toTaskSomeoneDetailsDTOConv = new Converter<TaskSomeone, TaskSomeoneDetailsDTO>() {

	      @Override
	      public TaskSomeoneDetailsDTO convert(MappingContext<TaskSomeone, TaskSomeoneDetailsDTO> context) {	    	    
	         //This custom converter replaces the one automatically created by ModelMapper,
	         //So we have to map each of the contact fields as well.
	         context.getDestination().setId(context.getSource().getId());

	         context.getDestination().setCreatedOn(context.getSource().getCreatedOn());

	         
	         return context.getDestination();
	      }
	   };
	   
	   Converter<TaskSomeoneDetailsDTO, TaskSomeone> fromTaskSomeoneDetailsDTOConv = new Converter<TaskSomeoneDetailsDTO, TaskSomeone>() {

		      @Override
		      public TaskSomeone convert(MappingContext<TaskSomeoneDetailsDTO, TaskSomeone> context) {	    	    
		         //This custom converter replaces the one automatically created by ModelMapper,
		         //So we have to map each of the contact fields as well.
		         context.getDestination().setId(context.getSource().getId());
		         context.getDestination().setCreatedOn(context.getSource().getCreatedOn());

		         
		         return context.getDestination();
		      }
		   };
}
