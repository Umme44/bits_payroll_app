import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ManualAttendanceEntryFormService } from './manual-attendance-entry-form.service';
import { ManualAttendanceEntryService } from '../service/manual-attendance-entry.service';
import { IManualAttendanceEntry } from '../manual-attendance-entry.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { ManualAttendanceEntryUpdateComponent } from './manual-attendance-entry-update.component';

describe('ManualAttendanceEntry Management Update Component', () => {
  let comp: ManualAttendanceEntryUpdateComponent;
  let fixture: ComponentFixture<ManualAttendanceEntryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let manualAttendanceEntryFormService: ManualAttendanceEntryFormService;
  let manualAttendanceEntryService: ManualAttendanceEntryService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ManualAttendanceEntryUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ManualAttendanceEntryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ManualAttendanceEntryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    manualAttendanceEntryFormService = TestBed.inject(ManualAttendanceEntryFormService);
    manualAttendanceEntryService = TestBed.inject(ManualAttendanceEntryService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const manualAttendanceEntry: IManualAttendanceEntry = { id: 456 };
      const employee: IEmployee = { id: 10374 };
      manualAttendanceEntry.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 84517 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ manualAttendanceEntry });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const manualAttendanceEntry: IManualAttendanceEntry = { id: 456 };
      const employee: IEmployee = { id: 54551 };
      manualAttendanceEntry.employee = employee;

      activatedRoute.data = of({ manualAttendanceEntry });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.manualAttendanceEntry).toEqual(manualAttendanceEntry);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManualAttendanceEntry>>();
      const manualAttendanceEntry = { id: 123 };
      jest.spyOn(manualAttendanceEntryFormService, 'getManualAttendanceEntry').mockReturnValue(manualAttendanceEntry);
      jest.spyOn(manualAttendanceEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manualAttendanceEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manualAttendanceEntry }));
      saveSubject.complete();

      // THEN
      expect(manualAttendanceEntryFormService.getManualAttendanceEntry).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(manualAttendanceEntryService.update).toHaveBeenCalledWith(expect.objectContaining(manualAttendanceEntry));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManualAttendanceEntry>>();
      const manualAttendanceEntry = { id: 123 };
      jest.spyOn(manualAttendanceEntryFormService, 'getManualAttendanceEntry').mockReturnValue({ id: null });
      jest.spyOn(manualAttendanceEntryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manualAttendanceEntry: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manualAttendanceEntry }));
      saveSubject.complete();

      // THEN
      expect(manualAttendanceEntryFormService.getManualAttendanceEntry).toHaveBeenCalled();
      expect(manualAttendanceEntryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManualAttendanceEntry>>();
      const manualAttendanceEntry = { id: 123 };
      jest.spyOn(manualAttendanceEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manualAttendanceEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(manualAttendanceEntryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
