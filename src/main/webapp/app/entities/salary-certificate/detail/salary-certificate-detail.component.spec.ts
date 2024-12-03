import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SalaryCertificateDetailComponent } from './salary-certificate-detail.component';

describe('SalaryCertificate Management Detail Component', () => {
  let comp: SalaryCertificateDetailComponent;
  let fixture: ComponentFixture<SalaryCertificateDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SalaryCertificateDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ salaryCertificate: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SalaryCertificateDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SalaryCertificateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load salaryCertificate on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.salaryCertificate).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
