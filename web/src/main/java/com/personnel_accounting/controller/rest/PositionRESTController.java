package com.personnel_accounting.controller.rest;

import com.personnel_accounting.department.DepartmentService;
import com.personnel_accounting.domain.Position;
import com.personnel_accounting.entity.dto.PositionDTO;
import com.personnel_accounting.exception.ExistingDataException;
import com.personnel_accounting.pagination.entity.Page;
import com.personnel_accounting.pagination.entity.PagingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/position")
public class PositionRESTController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping("/add")
    public ResponseEntity<?> addPosition(@Valid @RequestBody PositionDTO positionDTO) {
        Position position = departmentService.addPosition(conversionService.convert(positionDTO, Position.class));
        if (position.getId() == null)
            throw new ExistingDataException(messageSource.getMessage("position.alert.add", null, LocaleContextHolder.getLocale()));
        return new ResponseEntity<>(position, HttpStatus.OK);
    }

    @PostMapping("/get_all")
    public ResponseEntity<?> getPositions(@RequestBody PagingRequest pagingRequest) {
        return new ResponseEntity<>(getPage(departmentService.findAllPositionsWithSearchSorting(pagingRequest)
                .stream().map(position -> conversionService.convert(position, PositionDTO.class))
                .collect(Collectors.toList()), pagingRequest.getDraw()),
                HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editPosition(@Valid @RequestBody PositionDTO positionDTO, @PathVariable Long id) {
        positionDTO.setId(id);
        return new ResponseEntity<>(
                departmentService.editPosition(conversionService.convert(positionDTO, Position.class)), HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removePosition(@PathVariable Long id) {
        departmentService.removePositionById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Page<PositionDTO> getPage(List<PositionDTO> list, int draw) {
        Page<PositionDTO> page = new Page<>(list);
        page.setDraw(draw);
        return page;
    }
}
