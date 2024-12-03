import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FestivalBonusConfigFormService } from './festival-bonus-config-form.service';
import { FestivalBonusConfigService } from '../service/festival-bonus-config.service';
import { IFestivalBonusConfig } from '../festival-bonus-config.model';

import { FestivalBonusConfigUpdateComponent } from './festival-bonus-config-update.component';

describe('FestivalBonusConfig Management Update Component', () => {
  let comp: FestivalBonusConfigUpdateComponent;
  let fixture: ComponentFixture<FestivalBonusConfigUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let festivalBonusConfigFormService: FestivalBonusConfigFormService;
  let festivalBonusConfigService: FestivalBonusConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FestivalBonusConfigUpdateComponent],
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
      .overrideTemplate(FestivalBonusConfigUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FestivalBonusConfigUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    festivalBonusConfigFormService = TestBed.inject(FestivalBonusConfigFormService);
    festivalBonusConfigService = TestBed.inject(FestivalBonusConfigService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const festivalBonusConfig: IFestivalBonusConfig = { id: 456 };

      activatedRoute.data = of({ festivalBonusConfig });
      comp.ngOnInit();

      expect(comp.festivalBonusConfig).toEqual(festivalBonusConfig);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFestivalBonusConfig>>();
      const festivalBonusConfig = { id: 123 };
      jest.spyOn(festivalBonusConfigFormService, 'getFestivalBonusConfig').mockReturnValue(festivalBonusConfig);
      jest.spyOn(festivalBonusConfigService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ festivalBonusConfig });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: festivalBonusConfig }));
      saveSubject.complete();

      // THEN
      expect(festivalBonusConfigFormService.getFestivalBonusConfig).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(festivalBonusConfigService.update).toHaveBeenCalledWith(expect.objectContaining(festivalBonusConfig));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFestivalBonusConfig>>();
      const festivalBonusConfig = { id: 123 };
      jest.spyOn(festivalBonusConfigFormService, 'getFestivalBonusConfig').mockReturnValue({ id: null });
      jest.spyOn(festivalBonusConfigService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ festivalBonusConfig: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: festivalBonusConfig }));
      saveSubject.complete();

      // THEN
      expect(festivalBonusConfigFormService.getFestivalBonusConfig).toHaveBeenCalled();
      expect(festivalBonusConfigService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFestivalBonusConfig>>();
      const festivalBonusConfig = { id: 123 };
      jest.spyOn(festivalBonusConfigService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ festivalBonusConfig });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(festivalBonusConfigService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
