package com.mhouse.project.pethealthrecordtracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhouse.project.pethealthrecordtracker.controllers.HealthRecordController;
import com.mhouse.project.pethealthrecordtracker.entities.HealthRecord;
import com.mhouse.project.pethealthrecordtracker.entities.Pet;
import com.mhouse.project.pethealthrecordtracker.repositories.PetRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthRecordController.class)
public class RestControllerTest {

    @Autowired private ObjectMapper mapper;
    @MockBean private PetRepository petRepository;
    private MockMvc mockMvc;

    private HealthRecordController controller;
    private Date someDate;
    private HealthRecord vetVisit;
    private Pet pet;
    private Pet petUpdates;

    @BeforeEach
    public void setup() {
        someDate = new Date(1576941356);
        System.out.println(">>" + someDate);
        vetVisit = new HealthRecord(someDate, "Dr. Ashley S", "Cats Only VC",
                "Checkup");

        pet = new Pet("Leo", "American Shorthair", "Gray Striped", Arrays.asList(vetVisit),
                true, "77777");

        petUpdates = new Pet("Leo", "American Shorthair", "Gray Striped", Arrays.asList(vetVisit),
                false, "");

        controller = new HealthRecordController(petRepository);

        mockMvc = MockMvcBuilders.standaloneSetup(new HealthRecordController(petRepository)).build();
    }

    @Test
    public void getByIdReturnsCorrectResponse() throws Exception {
        Long ID = 12L;
        pet.setId(ID);
        given(petRepository.findById(pet.getId())).willReturn(Optional.of(pet));
        final ResultActions result = mockMvc.perform(get("http://localhost/pets/" + ID));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("name").value(pet.getName()));
        result.andExpect(jsonPath("breed").value(pet.getBreed()));
        result.andExpect(jsonPath("color").value(pet.getColor()));
        result.andExpect(jsonPath("records").value(Matchers.notNullValue()));
    }

    @Test
    public void getAllReturnsCorrectResponse() throws Exception {
        Long ID = 12L;
        pet.setId(ID);
        given(petRepository.findAll()).willReturn(Arrays.asList(pet));
        final ResultActions result = mockMvc.perform(get("http://localhost/pets/"));
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
        result.andExpect(status().isOk());
        result.andExpect(content().string(contains(mapper.writeValueAsString(pet))));
        assertThat(response).contains("Leo");
    }

    @Test
    public void postReturnsCorrectResponse() throws Exception {
        Long ID = 12L;
        pet.setId(ID);
        given(petRepository.save(pet)).willReturn(pet);
        final ResultActions result = mockMvc.perform(post("http://localhost/pets/").content(mapper.writeValueAsBytes(pet))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("name").value(pet.getName()));
        result.andExpect(jsonPath("breed").value(pet.getBreed()));
        result.andExpect(jsonPath("color").value(pet.getColor()));
        result.andExpect(jsonPath("records").value(Matchers.notNullValue()));
    }

    @Test
    public void putUpdatesRecordAndReturnsCorrectResponse() throws Exception {
        Long ID = 12L;
        pet.setId(ID);
        petUpdates.setId(ID);

        given(petRepository.save(pet)).willReturn(pet, petUpdates);
        given(petRepository.findById(ID)).willReturn(Optional.of(pet));

        final ResultActions postResult = mockMvc.perform(post("http://localhost/pets/").content(mapper.writeValueAsBytes(pet))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        final ResultActions putResult = mockMvc.perform(put("http://localhost/pets/" + ID).content(mapper.writeValueAsBytes(petUpdates))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        postResult.andExpect(status().isOk());

        putResult.andExpect(status().isOk());
        putResult.andExpect(jsonPath("name").value(petUpdates.getName()));
        putResult.andExpect(jsonPath("breed").value(petUpdates.getBreed()));
        putResult.andExpect(jsonPath("color").value(petUpdates.getColor()));
        putResult.andExpect(jsonPath("records").value(Matchers.notNullValue()));
        putResult.andExpect(jsonPath("hasMicrochip").value("false"));
    }
}
