import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CalenderYearFormService } from './calender-year-form.service';
import { CalenderYearService } from '../service/calender-year.service';
import { ICalenderYear } from '../calender-year.model';

import { CalenderYearUpdateComponent } from './calender-year-update.component';

describe('CalenderYear Management Update Component', () => {
  let comp: CalenderYearUpdateComponent;
  let fixture: ComponentFixture<CalenderYearUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let calenderYearFormService: CalenderYearFormService;
  let calenderYearService: CalenderYearService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CalenderYearUpdateComponent],
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
      .overrideTemplate(CalenderYearUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CalenderYearUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    calenderYearFormService = TestBed.inject(CalenderYearFormService);
    calenderYearService = TestBed.inject(CalenderYearService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const calenderYear: ICalenderYear = { id: 456 };

      activatedRoute.data = of({ calenderYear });
      comp.ngOnInit();

      expect(comp.calenderYear).toEqual(calenderYear);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICalenderYear>>();
      const calenderYear = { id: 123 };
      jest.spyOn(calenderYearFormService, 'getCalenderYear').mockReturnValue(calenderYear);
      jest.spyOn(calenderYearService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ calenderYear });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: calenderYear }));
      saveSubject.complete();

      // THEN
      expect(calenderYearFormService.getCalenderYear).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(calenderYearService.update).toHaveBeenCalledWith(expect.objectContaining(calenderYear));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICalenderYear>>();
      const calenderYear = { id: 123 };
      jest.spyOn(calenderYearFormService, 'getCalenderYear').mockReturnValue({ id: null });
      jest.spyOn(calenderYearService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ calenderYear: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: calenderYear }));
      saveSubject.complete();

      // THEN
      expect(calenderYearFormService.getCalenderYear).toHaveBeenCalled();
      expect(calenderYearService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICalenderYear>>();
      const calenderYear = { id: 123 };
      jest.spyOn(calenderYearService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ calenderYear });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(calenderYearService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
