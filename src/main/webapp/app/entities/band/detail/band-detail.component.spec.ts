import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BandDetailComponent } from './band-detail.component';

describe('Band Management Detail Component', () => {
  let comp: BandDetailComponent;
  let fixture: ComponentFixture<BandDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BandDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ band: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BandDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BandDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load band on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.band).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
