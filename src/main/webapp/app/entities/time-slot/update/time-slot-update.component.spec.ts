import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TimeSlotFormService } from './time-slot-form.service';
import { TimeSlotService } from '../service/time-slot.service';
import { ITimeSlot } from '../time-slot.model';

import { TimeSlotUpdateComponent } from './time-slot-update.component';

describe('TimeSlot Management Update Component', () => {
  let comp: TimeSlotUpdateComponent;
  let fixture: ComponentFixture<TimeSlotUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let timeSlotFormService: TimeSlotFormService;
  let timeSlotService: TimeSlotService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TimeSlotUpdateComponent],
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
      .overrideTemplate(TimeSlotUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TimeSlotUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    timeSlotFormService = TestBed.inject(TimeSlotFormService);
    timeSlotService = TestBed.inject(TimeSlotService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const timeSlot: ITimeSlot = { id: 456 };

      activatedRoute.data = of({ timeSlot });
      comp.ngOnInit();

      expect(comp.timeSlot).toEqual(timeSlot);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITimeSlot>>();
      const timeSlot = { id: 123 };
      jest.spyOn(timeSlotFormService, 'getTimeSlot').mockReturnValue(timeSlot);
      jest.spyOn(timeSlotService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ timeSlot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: timeSlot }));
      saveSubject.complete();

      // THEN
      expect(timeSlotFormService.getTimeSlot).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(timeSlotService.update).toHaveBeenCalledWith(expect.objectContaining(timeSlot));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITimeSlot>>();
      const timeSlot = { id: 123 };
      jest.spyOn(timeSlotFormService, 'getTimeSlot').mockReturnValue({ id: null });
      jest.spyOn(timeSlotService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ timeSlot: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: timeSlot }));
      saveSubject.complete();

      // THEN
      expect(timeSlotFormService.getTimeSlot).toHaveBeenCalled();
      expect(timeSlotService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITimeSlot>>();
      const timeSlot = { id: 123 };
      jest.spyOn(timeSlotService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ timeSlot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(timeSlotService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
