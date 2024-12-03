import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FestivalFormService } from './festival-form.service';
import { FestivalService } from '../service/festival.service';
import { IFestival } from '../festival.model';

import { FestivalUpdateComponent } from './festival-update.component';

describe('Festival Management Update Component', () => {
  let comp: FestivalUpdateComponent;
  let fixture: ComponentFixture<FestivalUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let festivalFormService: FestivalFormService;
  let festivalService: FestivalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FestivalUpdateComponent],
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
      .overrideTemplate(FestivalUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FestivalUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    festivalFormService = TestBed.inject(FestivalFormService);
    festivalService = TestBed.inject(FestivalService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const festival: IFestival = { id: 456 };

      activatedRoute.data = of({ festival });
      comp.ngOnInit();

      expect(comp.festival).toEqual(festival);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFestival>>();
      const festival = { id: 123 };
      jest.spyOn(festivalFormService, 'getFestival').mockReturnValue(festival);
      jest.spyOn(festivalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ festival });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: festival }));
      saveSubject.complete();

      // THEN
      expect(festivalFormService.getFestival).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(festivalService.update).toHaveBeenCalledWith(expect.objectContaining(festival));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFestival>>();
      const festival = { id: 123 };
      jest.spyOn(festivalFormService, 'getFestival').mockReturnValue({ id: null });
      jest.spyOn(festivalService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ festival: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: festival }));
      saveSubject.complete();

      // THEN
      expect(festivalFormService.getFestival).toHaveBeenCalled();
      expect(festivalService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFestival>>();
      const festival = { id: 123 };
      jest.spyOn(festivalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ festival });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(festivalService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
