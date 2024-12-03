import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FlexScheduleApplicationFormService } from './flex-schedule-application-form.service';
import { FlexScheduleApplicationService } from '../service/flex-schedule-application.service';
import { IFlexScheduleApplication } from '../flex-schedule-application.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITimeSlot } from 'app/entities/time-slot/time-slot.model';
import { TimeSlotService } from 'app/entities/time-slot/service/time-slot.service';

import { FlexScheduleApplicationUpdateComponent } from './flex-schedule-application-update.component';

describe('FlexScheduleApplication Management Update Component', () => {
  let comp: FlexScheduleApplicationUpdateComponent;
  let fixture: ComponentFixture<FlexScheduleApplicationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let flexScheduleApplicationFormService: FlexScheduleApplicationFormService;
  let flexScheduleApplicationService: FlexScheduleApplicationService;
  let employeeService: EmployeeService;
  let userService: UserService;
  let timeSlotService: TimeSlotService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FlexScheduleApplicationUpdateComponent],
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
      .overrideTemplate(FlexScheduleApplicationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FlexScheduleApplicationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    flexScheduleApplicationFormService = TestBed.inject(FlexScheduleApplicationFormService);
    flexScheduleApplicationService = TestBed.inject(FlexScheduleApplicationService);
    employeeService = TestBed.inject(EmployeeService);
    userService = TestBed.inject(UserService);
    timeSlotService = TestBed.inject(TimeSlotService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const flexScheduleApplication: IFlexScheduleApplication = { id: 456 };
      const requester: IEmployee = { id: 62273 };
      flexScheduleApplication.requester = requester;

      const employeeCollection: IEmployee[] = [{ id: 35739 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [requester];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ flexScheduleApplication });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const flexScheduleApplication: IFlexScheduleApplication = { id: 456 };
      const sanctionedBy: IUser = { id: 22347 };
      flexScheduleApplication.sanctionedBy = sanctionedBy;
      const appliedBy: IUser = { id: 34007 };
      flexScheduleApplication.appliedBy = appliedBy;
      const createdBy: IUser = { id: 17618 };
      flexScheduleApplication.createdBy = createdBy;
      const updatedBy: IUser = { id: 32249 };
      flexScheduleApplication.updatedBy = updatedBy;

      const userCollection: IUser[] = [{ id: 84988 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [sanctionedBy, appliedBy, createdBy, updatedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ flexScheduleApplication });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TimeSlot query and add missing value', () => {
      const flexScheduleApplication: IFlexScheduleApplication = { id: 456 };
      const timeSlot: ITimeSlot = { id: 47388 };
      flexScheduleApplication.timeSlot = timeSlot;

      const timeSlotCollection: ITimeSlot[] = [{ id: 16579 }];
      jest.spyOn(timeSlotService, 'query').mockReturnValue(of(new HttpResponse({ body: timeSlotCollection })));
      const additionalTimeSlots = [timeSlot];
      const expectedCollection: ITimeSlot[] = [...additionalTimeSlots, ...timeSlotCollection];
      jest.spyOn(timeSlotService, 'addTimeSlotToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ flexScheduleApplication });
      comp.ngOnInit();

      expect(timeSlotService.query).toHaveBeenCalled();
      expect(timeSlotService.addTimeSlotToCollectionIfMissing).toHaveBeenCalledWith(
        timeSlotCollection,
        ...additionalTimeSlots.map(expect.objectContaining)
      );
      expect(comp.timeSlotsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const flexScheduleApplication: IFlexScheduleApplication = { id: 456 };
      const requester: IEmployee = { id: 84569 };
      flexScheduleApplication.requester = requester;
      const sanctionedBy: IUser = { id: 48413 };
      flexScheduleApplication.sanctionedBy = sanctionedBy;
      const appliedBy: IUser = { id: 94706 };
      flexScheduleApplication.appliedBy = appliedBy;
      const createdBy: IUser = { id: 93435 };
      flexScheduleApplication.createdBy = createdBy;
      const updatedBy: IUser = { id: 79701 };
      flexScheduleApplication.updatedBy = updatedBy;
      const timeSlot: ITimeSlot = { id: 62417 };
      flexScheduleApplication.timeSlot = timeSlot;

      activatedRoute.data = of({ flexScheduleApplication });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(requester);
      expect(comp.usersSharedCollection).toContain(sanctionedBy);
      expect(comp.usersSharedCollection).toContain(appliedBy);
      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.timeSlotsSharedCollection).toContain(timeSlot);
      expect(comp.flexScheduleApplication).toEqual(flexScheduleApplication);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFlexScheduleApplication>>();
      const flexScheduleApplication = { id: 123 };
      jest.spyOn(flexScheduleApplicationFormService, 'getFlexScheduleApplication').mockReturnValue(flexScheduleApplication);
      jest.spyOn(flexScheduleApplicationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ flexScheduleApplication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: flexScheduleApplication }));
      saveSubject.complete();

      // THEN
      expect(flexScheduleApplicationFormService.getFlexScheduleApplication).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(flexScheduleApplicationService.update).toHaveBeenCalledWith(expect.objectContaining(flexScheduleApplication));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFlexScheduleApplication>>();
      const flexScheduleApplication = { id: 123 };
      jest.spyOn(flexScheduleApplicationFormService, 'getFlexScheduleApplication').mockReturnValue({ id: null });
      jest.spyOn(flexScheduleApplicationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ flexScheduleApplication: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: flexScheduleApplication }));
      saveSubject.complete();

      // THEN
      expect(flexScheduleApplicationFormService.getFlexScheduleApplication).toHaveBeenCalled();
      expect(flexScheduleApplicationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFlexScheduleApplication>>();
      const flexScheduleApplication = { id: 123 };
      jest.spyOn(flexScheduleApplicationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ flexScheduleApplication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(flexScheduleApplicationService.update).toHaveBeenCalled();
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

    describe('compareTimeSlot', () => {
      it('Should forward to timeSlotService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(timeSlotService, 'compareTimeSlot');
        comp.compareTimeSlot(entity, entity2);
        expect(timeSlotService.compareTimeSlot).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
