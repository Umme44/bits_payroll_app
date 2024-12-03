import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HolidaysFormService } from './holidays-form.service';
import { HolidaysService } from '../service/holidays.service';
import { IHolidays } from '../holidays.model';

import { HolidaysUpdateComponent } from './holidays-update.component';

describe('Holidays Management Update Component', () => {
  let comp: HolidaysUpdateComponent;
  let fixture: ComponentFixture<HolidaysUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let holidaysFormService: HolidaysFormService;
  let holidaysService: HolidaysService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HolidaysUpdateComponent],
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
      .overrideTemplate(HolidaysUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HolidaysUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    holidaysFormService = TestBed.inject(HolidaysFormService);
    holidaysService = TestBed.inject(HolidaysService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const holidays: IHolidays = { id: 456 };

      activatedRoute.data = of({ holidays });
      comp.ngOnInit();

      expect(comp.holidays).toEqual(holidays);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHolidays>>();
      const holidays = { id: 123 };
      jest.spyOn(holidaysFormService, 'getHolidays').mockReturnValue(holidays);
      jest.spyOn(holidaysService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ holidays });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: holidays }));
      saveSubject.complete();

      // THEN
      expect(holidaysFormService.getHolidays).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(holidaysService.update).toHaveBeenCalledWith(expect.objectContaining(holidays));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHolidays>>();
      const holidays = { id: 123 };
      jest.spyOn(holidaysFormService, 'getHolidays').mockReturnValue({ id: null });
      jest.spyOn(holidaysService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ holidays: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: holidays }));
      saveSubject.complete();

      // THEN
      expect(holidaysFormService.getHolidays).toHaveBeenCalled();
      expect(holidaysService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHolidays>>();
      const holidays = { id: 123 };
      jest.spyOn(holidaysService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ holidays });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(holidaysService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
