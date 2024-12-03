import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OfficeNoticesFormService } from './office-notices-form.service';
import { OfficeNoticesService } from '../service/office-notices.service';
import { IOfficeNotices } from '../office-notices.model';

import { OfficeNoticesUpdateComponent } from './office-notices-update.component';

describe('OfficeNotices Management Update Component', () => {
  let comp: OfficeNoticesUpdateComponent;
  let fixture: ComponentFixture<OfficeNoticesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let officeNoticesFormService: OfficeNoticesFormService;
  let officeNoticesService: OfficeNoticesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OfficeNoticesUpdateComponent],
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
      .overrideTemplate(OfficeNoticesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OfficeNoticesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    officeNoticesFormService = TestBed.inject(OfficeNoticesFormService);
    officeNoticesService = TestBed.inject(OfficeNoticesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const officeNotices: IOfficeNotices = { id: 456 };

      activatedRoute.data = of({ officeNotices });
      comp.ngOnInit();

      expect(comp.officeNotices).toEqual(officeNotices);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOfficeNotices>>();
      const officeNotices = { id: 123 };
      jest.spyOn(officeNoticesFormService, 'getOfficeNotices').mockReturnValue(officeNotices);
      jest.spyOn(officeNoticesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ officeNotices });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: officeNotices }));
      saveSubject.complete();

      // THEN
      expect(officeNoticesFormService.getOfficeNotices).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(officeNoticesService.update).toHaveBeenCalledWith(expect.objectContaining(officeNotices));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOfficeNotices>>();
      const officeNotices = { id: 123 };
      jest.spyOn(officeNoticesFormService, 'getOfficeNotices').mockReturnValue({ id: null });
      jest.spyOn(officeNoticesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ officeNotices: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: officeNotices }));
      saveSubject.complete();

      // THEN
      expect(officeNoticesFormService.getOfficeNotices).toHaveBeenCalled();
      expect(officeNoticesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOfficeNotices>>();
      const officeNotices = { id: 123 };
      jest.spyOn(officeNoticesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ officeNotices });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(officeNoticesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
