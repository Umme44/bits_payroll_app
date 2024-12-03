import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NationalityFormService } from './nationality-form.service';
import { NationalityService } from '../service/nationality.service';
import { INationality } from '../nationality.model';

import { NationalityUpdateComponent } from './nationality-update.component';

describe('Nationality Management Update Component', () => {
  let comp: NationalityUpdateComponent;
  let fixture: ComponentFixture<NationalityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let nationalityFormService: NationalityFormService;
  let nationalityService: NationalityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [NationalityUpdateComponent],
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
      .overrideTemplate(NationalityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NationalityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    nationalityFormService = TestBed.inject(NationalityFormService);
    nationalityService = TestBed.inject(NationalityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const nationality: INationality = { id: 456 };

      activatedRoute.data = of({ nationality });
      comp.ngOnInit();

      expect(comp.nationality).toEqual(nationality);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INationality>>();
      const nationality = { id: 123 };
      jest.spyOn(nationalityFormService, 'getNationality').mockReturnValue(nationality);
      jest.spyOn(nationalityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nationality });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nationality }));
      saveSubject.complete();

      // THEN
      expect(nationalityFormService.getNationality).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(nationalityService.update).toHaveBeenCalledWith(expect.objectContaining(nationality));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INationality>>();
      const nationality = { id: 123 };
      jest.spyOn(nationalityFormService, 'getNationality').mockReturnValue({ id: null });
      jest.spyOn(nationalityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nationality: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nationality }));
      saveSubject.complete();

      // THEN
      expect(nationalityFormService.getNationality).toHaveBeenCalled();
      expect(nationalityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INationality>>();
      const nationality = { id: 123 };
      jest.spyOn(nationalityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nationality });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(nationalityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
