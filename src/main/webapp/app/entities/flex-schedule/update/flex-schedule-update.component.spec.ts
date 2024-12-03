import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FlexScheduleFormService } from './flex-schedule-form.service';
import { FlexScheduleService } from '../service/flex-schedule.service';
import { IFlexSchedule } from '../flex-schedule.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { FlexScheduleUpdateComponent } from './flex-schedule-update.component';

describe('FlexSchedule Management Update Component', () => {
  let comp: FlexScheduleUpdateComponent;
  let fixture: ComponentFixture<FlexScheduleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let flexScheduleFormService: FlexScheduleFormService;
  let flexScheduleService: FlexScheduleService;
  let employeeService: EmployeeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FlexScheduleUpdateComponent],
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
      .overrideTemplate(FlexScheduleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FlexScheduleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    flexScheduleFormService = TestBed.inject(FlexScheduleFormService);
    flexScheduleService = TestBed.inject(FlexScheduleService);
    employeeService = TestBed.inject(EmployeeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const flexSchedule: IFlexSchedule = { id: 456 };
      const employee: IEmployee = { id: 20042 };
      flexSchedule.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 80645 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ flexSchedule });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const flexSchedule: IFlexSchedule = { id: 456 };
      const createdBy: IUser = { id: 58017 };
      flexSchedule.createdBy = createdBy;

      const userCollection: IUser[] = [{ id: 38163 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ flexSchedule });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const flexSchedule: IFlexSchedule = { id: 456 };
      const employee: IEmployee = { id: 86023 };
      flexSchedule.employee = employee;
      const createdBy: IUser = { id: 20470 };
      flexSchedule.createdBy = createdBy;

      activatedRoute.data = of({ flexSchedule });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.flexSchedule).toEqual(flexSchedule);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFlexSchedule>>();
      const flexSchedule = { id: 123 };
      jest.spyOn(flexScheduleFormService, 'getFlexSchedule').mockReturnValue(flexSchedule);
      jest.spyOn(flexScheduleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ flexSchedule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: flexSchedule }));
      saveSubject.complete();

      // THEN
      expect(flexScheduleFormService.getFlexSchedule).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(flexScheduleService.update).toHaveBeenCalledWith(expect.objectContaining(flexSchedule));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFlexSchedule>>();
      const flexSchedule = { id: 123 };
      jest.spyOn(flexScheduleFormService, 'getFlexSchedule').mockReturnValue({ id: null });
      jest.spyOn(flexScheduleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ flexSchedule: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: flexSchedule }));
      saveSubject.complete();

      // THEN
      expect(flexScheduleFormService.getFlexSchedule).toHaveBeenCalled();
      expect(flexScheduleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFlexSchedule>>();
      const flexSchedule = { id: 123 };
      jest.spyOn(flexScheduleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ flexSchedule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(flexScheduleService.update).toHaveBeenCalled();
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

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
