import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WorkingExperienceFormService } from './working-experience-form.service';
import { WorkingExperienceService } from '../service/working-experience.service';
import { IWorkingExperience } from '../working-experience.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { WorkingExperienceUpdateComponent } from './working-experience-update.component';

describe('WorkingExperience Management Update Component', () => {
  let comp: WorkingExperienceUpdateComponent;
  let fixture: ComponentFixture<WorkingExperienceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workingExperienceFormService: WorkingExperienceFormService;
  let workingExperienceService: WorkingExperienceService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WorkingExperienceUpdateComponent],
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
      .overrideTemplate(WorkingExperienceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkingExperienceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workingExperienceFormService = TestBed.inject(WorkingExperienceFormService);
    workingExperienceService = TestBed.inject(WorkingExperienceService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const workingExperience: IWorkingExperience = { id: 456 };
      const employee: IEmployee = { id: 61999 };
      workingExperience.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 860 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workingExperience });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const workingExperience: IWorkingExperience = { id: 456 };
      const employee: IEmployee = { id: 22252 };
      workingExperience.employee = employee;

      activatedRoute.data = of({ workingExperience });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.workingExperience).toEqual(workingExperience);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkingExperience>>();
      const workingExperience = { id: 123 };
      jest.spyOn(workingExperienceFormService, 'getWorkingExperience').mockReturnValue(workingExperience);
      jest.spyOn(workingExperienceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workingExperience });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workingExperience }));
      saveSubject.complete();

      // THEN
      expect(workingExperienceFormService.getWorkingExperience).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(workingExperienceService.update).toHaveBeenCalledWith(expect.objectContaining(workingExperience));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkingExperience>>();
      const workingExperience = { id: 123 };
      jest.spyOn(workingExperienceFormService, 'getWorkingExperience').mockReturnValue({ id: null });
      jest.spyOn(workingExperienceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workingExperience: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workingExperience }));
      saveSubject.complete();

      // THEN
      expect(workingExperienceFormService.getWorkingExperience).toHaveBeenCalled();
      expect(workingExperienceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkingExperience>>();
      const workingExperience = { id: 123 };
      jest.spyOn(workingExperienceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workingExperience });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workingExperienceService.update).toHaveBeenCalled();
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
