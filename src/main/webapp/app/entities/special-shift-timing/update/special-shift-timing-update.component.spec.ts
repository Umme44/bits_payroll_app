import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SpecialShiftTimingFormService } from './special-shift-timing-form.service';
import { SpecialShiftTimingService } from '../service/special-shift-timing.service';
import { ISpecialShiftTiming } from '../special-shift-timing.model';
import { ITimeSlot } from 'app/entities/time-slot/time-slot.model';
import { TimeSlotService } from 'app/entities/time-slot/service/time-slot.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { SpecialShiftTimingUpdateComponent } from './special-shift-timing-update.component';

describe('SpecialShiftTiming Management Update Component', () => {
  let comp: SpecialShiftTimingUpdateComponent;
  let fixture: ComponentFixture<SpecialShiftTimingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let specialShiftTimingFormService: SpecialShiftTimingFormService;
  let specialShiftTimingService: SpecialShiftTimingService;
  let timeSlotService: TimeSlotService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SpecialShiftTimingUpdateComponent],
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
      .overrideTemplate(SpecialShiftTimingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SpecialShiftTimingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    specialShiftTimingFormService = TestBed.inject(SpecialShiftTimingFormService);
    specialShiftTimingService = TestBed.inject(SpecialShiftTimingService);
    timeSlotService = TestBed.inject(TimeSlotService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TimeSlot query and add missing value', () => {
      const specialShiftTiming: ISpecialShiftTiming = { id: 456 };
      const timeSlot: ITimeSlot = { id: 91716 };
      specialShiftTiming.timeSlot = timeSlot;

      const timeSlotCollection: ITimeSlot[] = [{ id: 39152 }];
      jest.spyOn(timeSlotService, 'query').mockReturnValue(of(new HttpResponse({ body: timeSlotCollection })));
      const additionalTimeSlots = [timeSlot];
      const expectedCollection: ITimeSlot[] = [...additionalTimeSlots, ...timeSlotCollection];
      jest.spyOn(timeSlotService, 'addTimeSlotToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ specialShiftTiming });
      comp.ngOnInit();

      expect(timeSlotService.query).toHaveBeenCalled();
      expect(timeSlotService.addTimeSlotToCollectionIfMissing).toHaveBeenCalledWith(
        timeSlotCollection,
        ...additionalTimeSlots.map(expect.objectContaining)
      );
      expect(comp.timeSlotsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const specialShiftTiming: ISpecialShiftTiming = { id: 456 };
      const createdBy: IUser = { id: 15084 };
      specialShiftTiming.createdBy = createdBy;
      const updatedBy: IUser = { id: 14916 };
      specialShiftTiming.updatedBy = updatedBy;

      const userCollection: IUser[] = [{ id: 472 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ specialShiftTiming });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const specialShiftTiming: ISpecialShiftTiming = { id: 456 };
      const timeSlot: ITimeSlot = { id: 12388 };
      specialShiftTiming.timeSlot = timeSlot;
      const createdBy: IUser = { id: 42150 };
      specialShiftTiming.createdBy = createdBy;
      const updatedBy: IUser = { id: 61437 };
      specialShiftTiming.updatedBy = updatedBy;

      activatedRoute.data = of({ specialShiftTiming });
      comp.ngOnInit();

      expect(comp.timeSlotsSharedCollection).toContain(timeSlot);
      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.specialShiftTiming).toEqual(specialShiftTiming);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpecialShiftTiming>>();
      const specialShiftTiming = { id: 123 };
      jest.spyOn(specialShiftTimingFormService, 'getSpecialShiftTiming').mockReturnValue(specialShiftTiming);
      jest.spyOn(specialShiftTimingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ specialShiftTiming });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: specialShiftTiming }));
      saveSubject.complete();

      // THEN
      expect(specialShiftTimingFormService.getSpecialShiftTiming).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(specialShiftTimingService.update).toHaveBeenCalledWith(expect.objectContaining(specialShiftTiming));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpecialShiftTiming>>();
      const specialShiftTiming = { id: 123 };
      jest.spyOn(specialShiftTimingFormService, 'getSpecialShiftTiming').mockReturnValue({ id: null });
      jest.spyOn(specialShiftTimingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ specialShiftTiming: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: specialShiftTiming }));
      saveSubject.complete();

      // THEN
      expect(specialShiftTimingFormService.getSpecialShiftTiming).toHaveBeenCalled();
      expect(specialShiftTimingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpecialShiftTiming>>();
      const specialShiftTiming = { id: 123 };
      jest.spyOn(specialShiftTimingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ specialShiftTiming });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(specialShiftTimingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTimeSlot', () => {
      it('Should forward to timeSlotService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(timeSlotService, 'compareTimeSlot');
        comp.compareTimeSlot(entity, entity2);
        expect(timeSlotService.compareTimeSlot).toHaveBeenCalledWith(entity, entity2);
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
