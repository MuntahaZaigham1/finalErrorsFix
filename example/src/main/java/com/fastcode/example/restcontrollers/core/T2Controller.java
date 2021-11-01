package com.fastcode.example.restcontrollers.core;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fastcode.example.domain.core.t2.T2Id;
import com.fastcode.example.commons.search.SearchCriteria;
import com.fastcode.example.commons.search.SearchUtils;
import com.fastcode.example.commons.search.OffsetBasedPageRequest;
import com.fastcode.example.application.core.t2.IT2AppService;
import com.fastcode.example.application.core.t2.dto.*;
import java.util.*;
import java.time.*;
import com.fastcode.example.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/t2")
@RequiredArgsConstructor
public class T2Controller {

	@Qualifier("t2AppService")
	@NonNull protected final IT2AppService _t2AppService;
	@NonNull protected final LoggingHelper logHelper;

	@NonNull protected final Environment env;

    @PreAuthorize("hasAnyAuthority('T2ENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<CreateT2Output> create( @RequestBody @Valid CreateT2Input t2) {
		CreateT2Output output=_t2AppService.create(t2);
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete t2 ------------
	@PreAuthorize("hasAnyAuthority('T2ENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = {"application/json"})
	public void delete(@PathVariable String id) {

		T2Id t2id =_t2AppService.parseT2Key(id);
		Optional.ofNullable(t2id).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

		FindT2ByIdOutput output = _t2AppService.findById(t2id);
    	Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a t2 with a id=%s", id)));

		_t2AppService.delete(t2id);
    }


	// ------------ Update t2 ------------
    @PreAuthorize("hasAnyAuthority('T2ENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<UpdateT2Output> update(@PathVariable String id,  @RequestBody @Valid UpdateT2Input t2) {

		T2Id t2id =_t2AppService.parseT2Key(id);

		Optional.ofNullable(t2id).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

		FindT2ByIdOutput currentT2 = _t2AppService.findById(t2id);
		Optional.ofNullable(currentT2).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. T2 with id=%s not found.", id)));


		t2.setVersiono(currentT2.getVersiono());
		UpdateT2Output output = _t2AppService.update(t2id,t2);
		return new ResponseEntity(output, HttpStatus.OK);
	}
    

    @PreAuthorize("hasAnyAuthority('T2ENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity<FindT2ByIdOutput> findById(@PathVariable String id) {

		T2Id t2id =_t2AppService.parseT2Key(id);
		Optional.ofNullable(t2id).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

		FindT2ByIdOutput output = _t2AppService.findById(t2id);
    	Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

		return new ResponseEntity(output, HttpStatus.OK);
	}
    @PreAuthorize("hasAnyAuthority('T2ENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET, consumes = {"application/json"}, produces = {"application/json"})
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {

		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);

		return ResponseEntity.ok(_t2AppService.find(searchCriteria,Pageable));
	}
	

}


